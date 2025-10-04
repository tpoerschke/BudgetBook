package timkodiert.budgetbook.view.analysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import jakarta.inject.Inject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.knowm.xchart.AnnotationLine;
import org.knowm.xchart.AnnotationTextPanel;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import timkodiert.budgetbook.analysis.AnalysisPeriod;
import timkodiert.budgetbook.analysis.CategorySeriesGenerator;
import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.domain.model.BudgetType;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.table.cell.CurrencyTableCell;
import timkodiert.budgetbook.view.View;
import timkodiert.budgetbook.view.monthly_overview.TableData;
import timkodiert.budgetbook.view.monthly_overview.ToTableDataMapper;

public class AnalysisView implements View, Initializable {

    private final LanguageManager languageManager;
    private final Repository<Category> categoryRepository;
    private final CategorySeriesGenerator categorySeriesGenerator;

    @FXML
    private BorderPane root;
    @FXML
    private StackPane chartContainer;
    @FXML
    private ComboBox<AnalysisPeriod> periodComboBox;
    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TableView<TableData> turnoverTable;
    @FXML
    private TableColumn<TableData, String> turnoverPosition;
    @FXML
    private TableColumn<TableData, Number> turnoverValue;

    private CategoryChart chart;

    @Inject
    public AnalysisView(LanguageManager languageManager, Repository<Category> categoryRepository, CategorySeriesGenerator categorySeriesGenerator) {
        this.languageManager = languageManager;
        this.categoryRepository = categoryRepository;
        this.categorySeriesGenerator = categorySeriesGenerator;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        periodComboBox.setConverter(Converters.get(AnalysisPeriod.class));
        periodComboBox.getItems().addAll(AnalysisPeriod.values());
        categoryComboBox.getItems().addAll(categoryRepository.findAll());
        periodComboBox.valueProperty().addListener((observable, oldVal, newVal) -> updateChart());
        categoryComboBox.valueProperty().addListener((observable, oldVal, newVal) -> updateChart());

        periodComboBox.getSelectionModel().select(AnalysisPeriod.LAST_12_MONTH);

        turnoverPosition.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().position()));
        turnoverValue.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().value()));
        turnoverValue.setCellFactory(col -> new CurrencyTableCell<>());
    }

    private void updateChart() {
        AnalysisPeriod selectedPeriod = periodComboBox.getSelectionModel().getSelectedItem();
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        if (selectedPeriod == null || selectedCategory == null) {
            return;
        }

        chart = new CategoryChartBuilder().width(800)
                                          .height(600)
                                          .title(String.format(languageManager.get("analysisView.chart.title"), selectedCategory.getName()))
                                          .xAxisTitle(languageManager.get("analysisView.chart.xAxis.month"))
                                          .yAxisTitle(languageManager.get("analysisView.chart.yAxis.expenses"))
                                          .theme(Styler.ChartTheme.GGPlot2)
                                          .build();
        styleChart();

        List<MonthYear> monthYearList = selectedPeriod.getMonths();
        List<String> monthNameList = monthYearList.stream().map(monthYear -> languageManager.get(LanguageManager.MONTH_NAMES.get(monthYear.getMonth() - 1))).toList();
        CategorySeries s1 = chart.addSeries("Categories", monthNameList, categorySeriesGenerator.generateCategorySeries(selectedPeriod, selectedCategory));
        s1.setFillColor(Color.decode("#27476E"));

        CategorySeries s2 = chart.addSeries("Cumulative",
                                            monthNameList,
                                            categorySeriesGenerator.generateCumulativeCategorySeries(selectedPeriod, selectedCategory));
        s2.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
        s2.setLineColor(Color.decode("#16C172"));
        s2.setMarkerColor(Color.decode("#16C172"));

        chart.setYAxisGroupTitle(1, languageManager.get("analysisView.chart.yAxis.cumulativeExpenses"));
        s2.setYAxisGroup(1);
        chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

        addBudgetSeries();
        addAnnotations();

        XChartPanel<CategoryChart> panel = new XChartPanel<>(chart);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(panel);

        swingNode.setOnMouseClicked(this::fillTurnoverTable);

        chartContainer.getChildren().setAll(swingNode);
    }

    /**
     * Annotationslinien können nicht an der zweiten (rechten) Y-Achse ausgerichtet werden, da dies je nach
     * BudgetType jedoch nötig ist, wird die Budget-Linie als Series dargestellt.
     */
    private void addBudgetSeries() {
        AnalysisPeriod selectedPeriod = periodComboBox.getSelectionModel().getSelectedItem();
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        if (!selectedCategory.hasActiveBudget()) {
            return;
        }

        BudgetType budgetType = selectedCategory.getBudgetType();
        double budgetValue = selectedCategory.getBudgetValueInEuro();

        List<MonthYear> monthYearList = selectedPeriod.getMonths();
        List<String> monthNameList = monthYearList.stream().map(monthYear -> languageManager.get(LanguageManager.MONTH_NAMES.get(monthYear.getMonth() - 1))).toList();

        CategorySeries budgetSeries = chart.addSeries("Budget",
                                                      monthNameList,
                                                      Collections.nCopies(monthYearList.size(), budgetValue));

        if (budgetType == BudgetType.MONTHLY) {
            budgetSeries.setYAxisGroup(0);
        } else {
            budgetSeries.setYAxisGroup(1);
        }

        budgetSeries.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
        budgetSeries.setLineStyle(SeriesLines.DASH_DASH);
        budgetSeries.setMarker(SeriesMarkers.NONE);
        budgetSeries.setLineColor(Color.decode("#FF3900"));
        budgetSeries.setLineWidth(30.0f);
    }

    private void addAnnotations() {
        if (periodComboBox.getSelectionModel().getSelectedItem() == AnalysisPeriod.THIS_YEAR) {
            return;
        }
        YearMonth yearMonth = YearMonth.now();
        int xVal = 12 - yearMonth.getMonthValue() + 1;
        AnnotationLine yearLine = new AnnotationLine(xVal, true, false);
        String monthStr = String.format("%d →", yearMonth.getYear());
        AnnotationTextPanel annotationText = new AnnotationTextPanel(monthStr, xVal, 0, false);
        chart.addAnnotation(yearLine);
        chart.addAnnotation(annotationText);
    }

    private void styleChart() {
        chart.getStyler().setOverlapped(true);
        chart.getStyler().setLabelsVisible(true);
        chart.getStyler().setToolTipsEnabled(true);
        chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);
        chart.getStyler().setToolTipHighlightColor(Color.decode("#91B7C7"));
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setPlotGridVerticalLinesVisible(false);

        chart.getStyler().setAnnotationLineStroke(new BasicStroke(2));
        chart.getStyler().setAnnotationTextPanelBackgroundColor(Color.WHITE);
    }

    private void fillTurnoverTable(MouseEvent event) {
        AnalysisPeriod analysisPeriod = periodComboBox.getSelectionModel().getSelectedItem();
        int chartX = (int) chart.getChartXFromCoordinate((int) event.getScreenX());
        YearMonth yearMonth;
        if (analysisPeriod == AnalysisPeriod.LAST_12_MONTH) {
            yearMonth = YearMonth.now().minusMonths(12);
        } else {
            yearMonth = Year.now().atMonth(Month.JANUARY);
        }
        yearMonth = yearMonth.plusMonths((long) chartX - 1);
        MonthYear monthYear = MonthYear.of(yearMonth);

        Category category = categoryComboBox.getValue();
        List<TableData> fixedData = category.getFixedExpenses()
                                            .stream()
                                            .map(turnover -> ToTableDataMapper.mapFixedExpense(turnover, monthYear))
                                            .filter(tableData -> tableData.value() != 0.0)
                                            .toList();
        List<TableData> uniqueData = category.getUniqueTurnoverInformation(monthYear)
                                             .stream()
                                             .map(info -> ToTableDataMapper.mapUniqueExpense(info.getExpense()))
                                             .toList();
        turnoverTable.getItems().clear();
        turnoverTable.getItems().addAll(fixedData);
        turnoverTable.getItems().addAll(uniqueData);
    }
}

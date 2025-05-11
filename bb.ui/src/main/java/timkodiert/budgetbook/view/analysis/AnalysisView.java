package timkodiert.budgetbook.view.analysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import jakarta.inject.Inject;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import org.knowm.xchart.AnnotationLine;
import org.knowm.xchart.AnnotationTextPanel;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import timkodiert.budgetbook.analysis.AnalysisPeriod;
import timkodiert.budgetbook.analysis.CategorySeriesGenerator;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.View;

public class AnalysisView implements View, Initializable {

    private final LanguageManager languageManager;
    private final Repository<Category> categoryRepository;
    private final CategorySeriesGenerator categorySeriesGenerator;

    @FXML
    private BorderPane root;
    @FXML
    private ComboBox<AnalysisPeriod> periodComboBox;
    @FXML
    private ComboBox<Category> categoryComboBox;

    private CategoryChart chart;

    @Inject
    public AnalysisView(LanguageManager languageManager, Repository<Category> categoryRepository, CategorySeriesGenerator categorySeriesGenerator) {
        this.languageManager = languageManager;
        this.categoryRepository = categoryRepository;
        this.categorySeriesGenerator = categorySeriesGenerator;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        periodComboBox.getItems().addAll(AnalysisPeriod.values());
        categoryComboBox.getItems().addAll(categoryRepository.findAll());
        periodComboBox.valueProperty().addListener((observable, oldVal, newVal) -> updateChart());
        categoryComboBox.valueProperty().addListener((observable, oldVal, newVal) -> updateChart());
    }

    private void updateChart() {
        AnalysisPeriod selectedPeriod = periodComboBox.getSelectionModel().getSelectedItem();
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        if (selectedPeriod == null || selectedCategory == null) {
            return;
        }

        chart = new CategoryChartBuilder().width(800)
                                          .height(600)
                                          .title(String.format("Ausgaben der Kategorie \"%s\"", selectedCategory.getName()))
                                          .xAxisTitle("Monat")
                                          .yAxisTitle("Ausgaben (in Euro)")
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

        chart.setYAxisGroupTitle(1, "Kumulierte Ausgaben (in Euro)");
        s2.setYAxisGroup(1);
        chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

        XChartPanel<CategoryChart> panel = new XChartPanel<>(chart);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(panel);
        root.setCenter(swingNode);
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
        AnnotationLine annotationLine = new AnnotationLine(8, true, false);
        AnnotationTextPanel annotationText = new AnnotationTextPanel("2025 →", 8, -4, false);
        chart.addAnnotation(annotationLine);
        chart.addAnnotation(annotationText);
    }
}

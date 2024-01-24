package timkodiert.budgetBook.util;

import atlantafx.base.theme.Theme;

public record ThemeOption(String id, String name, Class<? extends Theme> theme) {}

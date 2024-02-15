package com.serezka.telegram.session.menu;

import java.util.List;

public record PageResponse(String text, List<PageButton> buttons, int columns) {}

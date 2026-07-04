package dev.injectref;

import java.nio.file.Path;

final class ReferenceFormatter {
    private ReferenceFormatter() {
    }

    static String formatRelativePath(String relativePath) {
        String normalized = relativePath.replace('\\', '/').replaceFirst("^/+", "");

        if (normalized.isEmpty()) {
            return null;
        }

        return normalized.startsWith(".") ? normalized : "@" + normalized;
    }

    static String formatAbsolutePath(String basePath, String filePath) {
        if (basePath == null || filePath == null) {
            return null;
        }

        Path base = Path.of(basePath).toAbsolutePath().normalize();
        Path file = Path.of(filePath).toAbsolutePath().normalize();

        if (!file.startsWith(base) || file.equals(base)) {
            return null;
        }

        return formatRelativePath(base.relativize(file).toString());
    }
}

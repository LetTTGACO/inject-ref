export function toAiReference(relativePath: string): string | undefined {
  const normalized = relativePath.replace(/\\/g, '/').replace(/^\/+/, '');

  if (normalized.length === 0) {
    return undefined;
  }

  return `@${normalized}`;
}

export function toSingleAiReference(copiedRelativePath: string): string | undefined {
  if (copiedRelativePath.includes('\n') || copiedRelativePath.includes('\r')) {
    return undefined;
  }

  return toAiReference(copiedRelativePath);
}

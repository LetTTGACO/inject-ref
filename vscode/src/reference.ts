export function toAiReference(relativePath: string): string | undefined {
  const normalized = relativePath.replace(/\\/g, '/').replace(/^\/+/, '');

  if (normalized.length === 0) {
    return undefined;
  }

  return `@${normalized}`;
}

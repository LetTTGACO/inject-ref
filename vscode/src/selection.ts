export interface CopyRequestState {
  selectedCount: number;
  isDirectory: boolean;
}

export function shouldRejectCopyRequest({ selectedCount, isDirectory }: CopyRequestState): boolean {
  return isDirectory || selectedCount > 1;
}

export interface CopyRequestState {
  selectedCount: number;
  isDirectory: boolean;
}

export function shouldRejectCopyRequest({ selectedCount }: CopyRequestState): boolean {
  return selectedCount > 1;
}

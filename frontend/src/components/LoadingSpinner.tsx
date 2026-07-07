export function LoadingSpinner() {
  return (
    <div className="flex h-full min-h-[40vh] w-full items-center justify-center">
      <div className="h-8 w-8 animate-spin rounded-full border-2 border-slate-300 border-t-slate-700" />
    </div>
  );
}

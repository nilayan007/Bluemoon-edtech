import { useEffect, useState } from 'react';
import { approveEnrollment, pendingEnrollments, rejectEnrollment } from '../../api/enrollments';
import { extractErrorMessage } from '../../api/client';
import type { EnrollmentResponseDTO } from '../../types/dto';
import { ErrorBanner } from '../../components/ErrorBanner';
import { LoadingSpinner } from '../../components/LoadingSpinner';

export function AdminEnrollments() {
  const [enrollments, setEnrollments] = useState<EnrollmentResponseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [busyId, setBusyId] = useState<number | null>(null);

  async function load() {
    setIsLoading(true);
    setError(null);
    try {
      setEnrollments(await pendingEnrollments());
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function handleApprove(enrollment: EnrollmentResponseDTO) {
    setBusyId(enrollment.id);
    setError(null);
    try {
      await approveEnrollment(enrollment.id);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setBusyId(null);
    }
  }

  async function handleReject(enrollment: EnrollmentResponseDTO) {
    setBusyId(enrollment.id);
    setError(null);
    try {
      await rejectEnrollment(enrollment.id);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setBusyId(null);
    }
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-10">
      <h1 className="mb-6 text-2xl font-semibold text-slate-900">Pending Enrollments</h1>
      <ErrorBanner message={error} />
      {isLoading ? (
        <LoadingSpinner />
      ) : enrollments.length === 0 ? (
        <p className="text-sm text-slate-500">No pending enrollment requests.</p>
      ) : (
        <ul className="flex flex-col gap-2">
          {enrollments.map((enrollment) => (
            <li
              key={enrollment.id}
              className="flex items-center justify-between rounded-lg border border-slate-200 px-4 py-3"
            >
              <div>
                <p className="font-medium text-slate-900">{enrollment.courseTitle}</p>
                <p className="text-sm text-slate-500">
                  {enrollment.userName} &middot; {enrollment.userEmail}
                </p>
              </div>
              <div className="flex gap-2">
                <button
                  onClick={() => handleApprove(enrollment)}
                  disabled={busyId === enrollment.id}
                  className="rounded-md bg-emerald-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-emerald-700 disabled:opacity-50"
                >
                  Approve
                </button>
                <button
                  onClick={() => handleReject(enrollment)}
                  disabled={busyId === enrollment.id}
                  className="rounded-md bg-red-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-red-700 disabled:opacity-50"
                >
                  Reject
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

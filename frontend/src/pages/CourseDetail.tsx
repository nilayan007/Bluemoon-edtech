import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getCourse } from '../api/courses';
import { myCourses, requestAccess } from '../api/enrollments';
import { extractErrorMessage } from '../api/client';
import type { CourseResponseDTO } from '../types/dto';
import { useAuth } from '../context/AuthContext';
import { ErrorBanner } from '../components/ErrorBanner';
import { LoadingSpinner } from '../components/LoadingSpinner';

export function CourseDetail() {
  const { courseId } = useParams<{ courseId: string }>();
  const id = Number(courseId);
  const { user } = useAuth();

  const [course, setCourse] = useState<CourseResponseDTO | null>(null);
  const [isEnrolled, setIsEnrolled] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [statusMessage, setStatusMessage] = useState<string | null>(null);
  const [isRequesting, setIsRequesting] = useState(false);

  const load = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const courseData = await getCourse(id);
      setCourse(courseData);
      if (user) {
        const active = await myCourses();
        setIsEnrolled(active.some((c) => c.id === id));
      }
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }, [id, user]);

  useEffect(() => {
    load();
  }, [load]);

  async function handleRequestAccess() {
    setIsRequesting(true);
    setStatusMessage(null);
    setError(null);
    try {
      const message = await requestAccess(id);
      setStatusMessage(message);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsRequesting(false);
    }
  }

  if (isLoading) return <LoadingSpinner />;
  if (!course) return <ErrorBanner message={error ?? 'Course not found'} />;

  return (
    <div className="mx-auto max-w-3xl px-4 py-10">
      <div className="aspect-video w-full overflow-hidden rounded-lg bg-slate-100">
        {course.thumbnailUrl && (
          <img src={course.thumbnailUrl} alt="" className="h-full w-full object-cover" />
        )}
      </div>
      <h1 className="mt-6 text-2xl font-semibold text-slate-900">{course.title}</h1>
      <p className="mt-2 whitespace-pre-line text-slate-600">{course.description}</p>

      <div className="mt-6 flex flex-col items-start gap-3">
        {!user && (
          <Link
            to="/login"
            className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700"
          >
            Log in to enroll
          </Link>
        )}
        {user && isEnrolled && (
          <Link
            to={`/learn/${course.id}`}
            className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700"
          >
            Go to course
          </Link>
        )}
        {user && !isEnrolled && (
          <button
            onClick={handleRequestAccess}
            disabled={isRequesting}
            className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700 disabled:opacity-50"
          >
            {isRequesting ? 'Requesting…' : 'Request access'}
          </button>
        )}
        {statusMessage && <p className="text-sm text-emerald-700">{statusMessage}</p>}
        <ErrorBanner message={error} />
      </div>
    </div>
  );
}

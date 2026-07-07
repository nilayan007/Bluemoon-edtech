import { useEffect, useState } from 'react';
import { myCourses } from '../api/enrollments';
import { extractErrorMessage } from '../api/client';
import type { CourseResponseDTO } from '../types/dto';
import { CourseCard } from '../components/CourseCard';
import { ErrorBanner } from '../components/ErrorBanner';
import { LoadingSpinner } from '../components/LoadingSpinner';

export function MyCourses() {
  const [courses, setCourses] = useState<CourseResponseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    myCourses()
      .then(setCourses)
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setIsLoading(false));
  }, []);

  return (
    <div className="mx-auto max-w-5xl px-4 py-10">
      <h1 className="mb-6 text-2xl font-semibold text-slate-900">My Courses</h1>
      <ErrorBanner message={error} />
      {isLoading ? (
        <LoadingSpinner />
      ) : courses.length === 0 ? (
        <p className="text-sm text-slate-500">
          You don't have any active enrollments yet. Browse the catalog to request access.
        </p>
      ) : (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {courses.map((course) => (
            <CourseCard key={course.id} course={course} />
          ))}
        </div>
      )}
    </div>
  );
}

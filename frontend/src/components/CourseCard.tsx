import { Link } from 'react-router-dom';
import type { CourseResponseDTO } from '../types/dto';

export function CourseCard({ course }: { course: CourseResponseDTO }) {
  return (
    <Link
      to={`/courses/${course.id}`}
      className="flex flex-col overflow-hidden rounded-lg border border-slate-200 bg-white transition hover:shadow-md"
    >
      <div className="aspect-video w-full bg-slate-100">
        {course.thumbnailUrl ? (
          <img
            src={course.thumbnailUrl}
            alt=""
            className="h-full w-full object-cover"
          />
        ) : (
          <div className="flex h-full items-center justify-center text-slate-300">No image</div>
        )}
      </div>
      <div className="flex flex-1 flex-col gap-1 p-4">
        <h3 className="font-semibold text-slate-900">{course.title}</h3>
        <p className="line-clamp-3 text-sm text-slate-500">{course.description}</p>
      </div>
    </Link>
  );
}

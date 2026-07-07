import { useEffect, useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';
import { listLessons } from '../api/lessons';
import { getCourse } from '../api/courses';
import { extractErrorMessage } from '../api/client';
import type { CourseResponseDTO, LessonResponseDTO } from '../types/dto';
import { LessonList } from '../components/LessonList';
import { ErrorBanner } from '../components/ErrorBanner';
import { LoadingSpinner } from '../components/LoadingSpinner';

function toEmbedUrl(url: string): string | null {
  const youtubeMatch = url.match(/(?:youtu\.be\/|youtube\.com\/watch\?v=)([\w-]+)/);
  if (youtubeMatch) return `https://www.youtube.com/embed/${youtubeMatch[1]}`;
  return null;
}

export function CourseLearn() {
  const { courseId } = useParams<{ courseId: string }>();
  const id = Number(courseId);

  const [course, setCourse] = useState<CourseResponseDTO | null>(null);
  const [lessons, setLessons] = useState<LessonResponseDTO[]>([]);
  const [activeLesson, setActiveLesson] = useState<LessonResponseDTO | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    Promise.all([getCourse(id), listLessons(id)])
      .then(([courseData, lessonData]) => {
        setCourse(courseData);
        setLessons(lessonData);
        const sorted = [...lessonData].sort((a, b) => a.orderIndex - b.orderIndex);
        setActiveLesson(sorted[0] ?? null);
      })
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setIsLoading(false));
  }, [id]);

  const embedUrl = useMemo(
    () => (activeLesson ? toEmbedUrl(activeLesson.videoUrl) : null),
    [activeLesson],
  );

  if (isLoading) return <LoadingSpinner />;
  if (error) return <ErrorBanner message={error} />;

  return (
    <div className="mx-auto grid max-w-5xl grid-cols-1 gap-6 px-4 py-10 md:grid-cols-[1fr_280px]">
      <div>
        <h1 className="mb-4 text-xl font-semibold text-slate-900">{course?.title}</h1>
        {activeLesson ? (
          <>
            <div className="aspect-video w-full overflow-hidden rounded-lg bg-black">
              {embedUrl ? (
                <iframe
                  src={embedUrl}
                  title={activeLesson.title}
                  className="h-full w-full"
                  allowFullScreen
                />
              ) : (
                <video src={activeLesson.videoUrl} controls className="h-full w-full" />
              )}
            </div>
            <h2 className="mt-4 font-medium text-slate-900">{activeLesson.title}</h2>
          </>
        ) : (
          <p className="text-sm text-slate-500">This course doesn't have any lessons yet.</p>
        )}
      </div>
      <aside>
        <h2 className="mb-2 text-sm font-semibold text-slate-500">Lessons</h2>
        <LessonList
          lessons={lessons}
          activeLessonId={activeLesson?.id ?? null}
          onSelect={setActiveLesson}
        />
      </aside>
    </div>
  );
}

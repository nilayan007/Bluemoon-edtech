import type { LessonResponseDTO } from '../types/dto';

interface LessonListProps {
  lessons: LessonResponseDTO[];
  activeLessonId: number | null;
  onSelect: (lesson: LessonResponseDTO) => void;
}

export function LessonList({ lessons, activeLessonId, onSelect }: LessonListProps) {
  const sorted = [...lessons].sort((a, b) => a.orderIndex - b.orderIndex);

  return (
    <ul className="flex flex-col gap-1">
      {sorted.map((lesson, index) => (
        <li key={lesson.id}>
          <button
            onClick={() => onSelect(lesson)}
            className={`flex w-full items-center gap-3 rounded-md px-3 py-2 text-left text-sm ${
              lesson.id === activeLessonId
                ? 'bg-slate-900 text-white'
                : 'text-slate-700 hover:bg-slate-100'
            }`}
          >
            <span className="text-xs opacity-70">{index + 1}.</span>
            <span className="flex-1 truncate">{lesson.title}</span>
          </button>
        </li>
      ))}
    </ul>
  );
}

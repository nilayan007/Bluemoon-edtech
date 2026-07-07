import { useEffect, useState, type FormEvent } from 'react';
import { Link, useParams } from 'react-router-dom';
import {
  addLesson,
  deleteLesson,
  listLessonsAdmin,
  updateLesson,
} from '../../api/lessons';
import { extractErrorMessage } from '../../api/client';
import type { LessonResponseDTO } from '../../types/dto';
import { ErrorBanner } from '../../components/ErrorBanner';
import { LoadingSpinner } from '../../components/LoadingSpinner';

interface LessonFormState {
  title: string;
  videoUrl: string;
  orderIndex: string;
}

const emptyForm: LessonFormState = { title: '', videoUrl: '', orderIndex: '' };

export function AdminCourseLessons() {
  const { courseId } = useParams<{ courseId: string }>();
  const id = Number(courseId);

  const [lessons, setLessons] = useState<LessonResponseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showCreateForm, setShowCreateForm] = useState(false);
  const [createForm, setCreateForm] = useState<LessonFormState>(emptyForm);

  const [editingLesson, setEditingLesson] = useState<LessonResponseDTO | null>(null);
  const [editForm, setEditForm] = useState<LessonFormState>(emptyForm);

  const [isSubmitting, setIsSubmitting] = useState(false);

  async function load() {
    setIsLoading(true);
    setError(null);
    try {
      const data = await listLessonsAdmin(id);
      setLessons([...data].sort((a, b) => a.orderIndex - b.orderIndex));
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  async function handleCreate(e: FormEvent) {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);
    try {
      await addLesson(id, {
        title: createForm.title,
        videoUrl: createForm.videoUrl,
        orderIndex: Number(createForm.orderIndex),
      });
      setCreateForm(emptyForm);
      setShowCreateForm(false);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsSubmitting(false);
    }
  }

  function startEdit(lesson: LessonResponseDTO) {
    setEditingLesson(lesson);
    setEditForm({
      title: lesson.title,
      videoUrl: lesson.videoUrl,
      orderIndex: String(lesson.orderIndex),
    });
  }

  async function handleEditSave(e: FormEvent) {
    e.preventDefault();
    if (!editingLesson) return;
    setIsSubmitting(true);
    setError(null);
    try {
      await updateLesson(editingLesson.id, {
        title: editForm.title,
        videoUrl: editForm.videoUrl,
        orderIndex: Number(editForm.orderIndex),
      });
      setEditingLesson(null);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleDelete(lesson: LessonResponseDTO) {
    if (!confirm(`Delete lesson "${lesson.title}"?`)) return;
    setError(null);
    try {
      await deleteLesson(lesson.id);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-10">
      <Link to="/admin/courses" className="text-sm text-slate-500 hover:text-slate-800">
        &larr; Back to courses
      </Link>
      <div className="mb-6 mt-2 flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-slate-900">Lessons</h1>
        <button
          onClick={() => setShowCreateForm((v) => !v)}
          className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700"
        >
          {showCreateForm ? 'Cancel' : 'Add lesson'}
        </button>
      </div>

      <ErrorBanner message={error} />

      {showCreateForm && (
        <form
          onSubmit={handleCreate}
          className="mb-8 flex flex-col gap-3 rounded-lg border border-slate-200 p-4"
        >
          <input
            required
            placeholder="Title"
            value={createForm.title}
            onChange={(e) => setCreateForm({ ...createForm, title: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <input
            required
            placeholder="Video URL"
            value={createForm.videoUrl}
            onChange={(e) => setCreateForm({ ...createForm, videoUrl: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <input
            required
            type="number"
            min={1}
            placeholder="Order"
            value={createForm.orderIndex}
            onChange={(e) => setCreateForm({ ...createForm, orderIndex: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <button
            type="submit"
            disabled={isSubmitting}
            className="w-fit rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700 disabled:opacity-50"
          >
            Add
          </button>
        </form>
      )}

      {editingLesson && (
        <form
          onSubmit={handleEditSave}
          className="mb-8 flex flex-col gap-3 rounded-lg border border-slate-200 p-4"
        >
          <h2 className="text-sm font-semibold text-slate-500">Editing {editingLesson.title}</h2>
          <input
            required
            placeholder="Title"
            value={editForm.title}
            onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <input
            required
            placeholder="Video URL"
            value={editForm.videoUrl}
            onChange={(e) => setEditForm({ ...editForm, videoUrl: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <input
            required
            type="number"
            min={1}
            placeholder="Order"
            value={editForm.orderIndex}
            onChange={(e) => setEditForm({ ...editForm, orderIndex: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <div className="flex gap-2">
            <button
              type="submit"
              disabled={isSubmitting}
              className="w-fit rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700 disabled:opacity-50"
            >
              Save
            </button>
            <button
              type="button"
              onClick={() => setEditingLesson(null)}
              className="w-fit rounded-md border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
            >
              Cancel
            </button>
          </div>
        </form>
      )}

      {isLoading ? (
        <LoadingSpinner />
      ) : lessons.length === 0 ? (
        <p className="text-sm text-slate-500">No lessons yet.</p>
      ) : (
        <ul className="flex flex-col gap-2">
          {lessons.map((lesson) => (
            <li
              key={lesson.id}
              className="flex items-center justify-between rounded-lg border border-slate-200 px-4 py-3"
            >
              <div>
                <span className="mr-2 text-xs text-slate-400">#{lesson.orderIndex}</span>
                <span className="text-slate-900">{lesson.title}</span>
              </div>
              <div className="flex gap-3 text-sm">
                <button
                  onClick={() => startEdit(lesson)}
                  className="text-slate-600 underline hover:text-slate-900"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(lesson)}
                  className="text-red-600 underline hover:text-red-800"
                >
                  Delete
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

import { useEffect, useState, type FormEvent } from 'react';
import { Link } from 'react-router-dom';
import {
  createCourse,
  deleteCourse,
  listAllCoursesAdmin,
  publishCourse,
  unpublishCourse,
  updateCourse,
} from '../../api/courses';
import { extractErrorMessage } from '../../api/client';
import type { CourseResponseDTO } from '../../types/dto';
import { ErrorBanner } from '../../components/ErrorBanner';
import { LoadingSpinner } from '../../components/LoadingSpinner';

interface CourseFormState {
  title: string;
  description: string;
  thumbnailUrl: string;
}

const emptyForm: CourseFormState = { title: '', description: '', thumbnailUrl: '' };

export function AdminCourses() {
  const [courses, setCourses] = useState<CourseResponseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showCreateForm, setShowCreateForm] = useState(false);
  const [createForm, setCreateForm] = useState<CourseFormState>(emptyForm);

  const [editingCourse, setEditingCourse] = useState<CourseResponseDTO | null>(null);
  const [editForm, setEditForm] = useState<CourseFormState>(emptyForm);

  const [isSubmitting, setIsSubmitting] = useState(false);

  async function load() {
    setIsLoading(true);
    setError(null);
    try {
      setCourses(await listAllCoursesAdmin());
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function handleCreate(e: FormEvent) {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);
    try {
      await createCourse(createForm);
      setCreateForm(emptyForm);
      setShowCreateForm(false);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsSubmitting(false);
    }
  }

  function startEdit(course: CourseResponseDTO) {
    setEditingCourse(course);
    setEditForm({
      title: course.title,
      description: course.description ?? '',
      thumbnailUrl: course.thumbnailUrl ?? '',
    });
  }

  async function handleEditSave(e: FormEvent) {
    e.preventDefault();
    if (!editingCourse) return;
    setIsSubmitting(true);
    setError(null);
    try {
      await updateCourse(editingCourse.id, editForm);
      setEditingCourse(null);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsSubmitting(false);
    }
  }

  async function handleTogglePublish(course: CourseResponseDTO) {
    setError(null);
    try {
      if (course.published) {
        await unpublishCourse(course.id);
      } else {
        await publishCourse(course.id);
      }
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  }

  async function handleDelete(course: CourseResponseDTO) {
    if (!confirm(`Delete "${course.title}"? This cannot be undone.`)) return;
    setError(null);
    try {
      await deleteCourse(course.id);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  }

  return (
    <div className="mx-auto max-w-5xl px-4 py-10">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-slate-900">Manage Courses</h1>
        <button
          onClick={() => setShowCreateForm((v) => !v)}
          className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700"
        >
          {showCreateForm ? 'Cancel' : 'New course'}
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
          <textarea
            placeholder="Description"
            value={createForm.description}
            onChange={(e) => setCreateForm({ ...createForm, description: e.target.value })}
            rows={3}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <input
            placeholder="Thumbnail URL"
            value={createForm.thumbnailUrl}
            onChange={(e) => setCreateForm({ ...createForm, thumbnailUrl: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <button
            type="submit"
            disabled={isSubmitting}
            className="w-fit rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700 disabled:opacity-50"
          >
            Create
          </button>
        </form>
      )}

      {editingCourse && (
        <form
          onSubmit={handleEditSave}
          className="mb-8 flex flex-col gap-3 rounded-lg border border-slate-200 p-4"
        >
          <h2 className="text-sm font-semibold text-slate-500">Editing {editingCourse.title}</h2>
          <input
            required
            placeholder="Title"
            value={editForm.title}
            onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <textarea
            placeholder="Description"
            value={editForm.description}
            onChange={(e) => setEditForm({ ...editForm, description: e.target.value })}
            rows={3}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          <input
            placeholder="Thumbnail URL"
            value={editForm.thumbnailUrl}
            onChange={(e) => setEditForm({ ...editForm, thumbnailUrl: e.target.value })}
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
              onClick={() => setEditingCourse(null)}
              className="w-fit rounded-md border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
            >
              Cancel
            </button>
          </div>
        </form>
      )}

      {isLoading ? (
        <LoadingSpinner />
      ) : (
        <div className="overflow-hidden rounded-lg border border-slate-200">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-50 text-slate-500">
              <tr>
                <th className="px-4 py-2 font-medium">Title</th>
                <th className="px-4 py-2 font-medium">Status</th>
                <th className="px-4 py-2 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              {courses.map((course) => (
                <tr key={course.id} className="border-t border-slate-200">
                  <td className="px-4 py-2 text-slate-900">{course.title}</td>
                  <td className="px-4 py-2">
                    <span
                      className={`rounded-full px-2 py-0.5 text-xs font-medium ${
                        course.published
                          ? 'bg-emerald-100 text-emerald-700'
                          : 'bg-slate-100 text-slate-600'
                      }`}
                    >
                      {course.published ? 'Published' : 'Draft'}
                    </span>
                  </td>
                  <td className="flex flex-wrap gap-3 px-4 py-2">
                    <Link
                      to={`/admin/courses/${course.id}/lessons`}
                      className="text-slate-600 underline hover:text-slate-900"
                    >
                      Lessons
                    </Link>
                    <button
                      onClick={() => startEdit(course)}
                      className="text-slate-600 underline hover:text-slate-900"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleTogglePublish(course)}
                      className="text-slate-600 underline hover:text-slate-900"
                    >
                      {course.published ? 'Unpublish' : 'Publish'}
                    </button>
                    <button
                      onClick={() => handleDelete(course)}
                      className="text-red-600 underline hover:text-red-800"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

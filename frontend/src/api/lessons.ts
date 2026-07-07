import { apiClient } from './client';
import type { AddLessonRequest, LessonResponseDTO, UpdateLessonRequest } from '../types/dto';

export async function listLessons(courseId: number): Promise<LessonResponseDTO[]> {
  const { data } = await apiClient.get<LessonResponseDTO[]>(`/courses/${courseId}/lessons`);
  return data;
}

export async function listLessonsAdmin(courseId: number): Promise<LessonResponseDTO[]> {
  const { data } = await apiClient.get<LessonResponseDTO[]>(`/admin/courses/${courseId}/lessons`);
  return data;
}

export async function addLesson(
  courseId: number,
  payload: AddLessonRequest,
): Promise<LessonResponseDTO> {
  const { data } = await apiClient.post<LessonResponseDTO>(
    `/admin/courses/${courseId}/lessons`,
    payload,
  );
  return data;
}

export async function updateLesson(
  lessonId: number,
  payload: UpdateLessonRequest,
): Promise<LessonResponseDTO> {
  const { data } = await apiClient.put<LessonResponseDTO>(`/admin/lessons/${lessonId}`, payload);
  return data;
}

export async function deleteLesson(lessonId: number): Promise<void> {
  await apiClient.delete(`/admin/lessons/${lessonId}`);
}

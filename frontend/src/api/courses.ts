import { apiClient } from './client';
import type { CourseResponseDTO, CreateCourseRequest, UpdateCourseRequest } from '../types/dto';

export async function listPublishedCourses(): Promise<CourseResponseDTO[]> {
  const { data } = await apiClient.get<CourseResponseDTO[]>('/courses');
  return data;
}

export async function getCourse(courseId: number): Promise<CourseResponseDTO> {
  const { data } = await apiClient.get<CourseResponseDTO>(`/courses/${courseId}`);
  return data;
}

export async function listAllCoursesAdmin(): Promise<CourseResponseDTO[]> {
  const { data } = await apiClient.get<CourseResponseDTO[]>('/admin/courses');
  return data;
}

export async function createCourse(payload: CreateCourseRequest): Promise<CourseResponseDTO> {
  const { data } = await apiClient.post<CourseResponseDTO>('/admin/courses', payload);
  return data;
}

export async function updateCourse(
  courseId: number,
  payload: UpdateCourseRequest,
): Promise<CourseResponseDTO> {
  const { data } = await apiClient.put<CourseResponseDTO>(`/admin/courses/${courseId}`, payload);
  return data;
}

export async function deleteCourse(courseId: number): Promise<void> {
  await apiClient.delete(`/admin/courses/${courseId}`);
}

export async function publishCourse(courseId: number): Promise<CourseResponseDTO> {
  const { data } = await apiClient.put<CourseResponseDTO>(`/admin/courses/${courseId}/publish`);
  return data;
}

export async function unpublishCourse(courseId: number): Promise<CourseResponseDTO> {
  const { data } = await apiClient.put<CourseResponseDTO>(`/admin/courses/${courseId}/unpublish`);
  return data;
}

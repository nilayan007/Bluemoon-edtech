import { apiClient } from './client';
import type { CourseResponseDTO, EnrollmentResponseDTO } from '../types/dto';

export async function requestAccess(courseId: number): Promise<string> {
  const { data } = await apiClient.post<string>(`/user/courses/${courseId}/request-access`);
  return data;
}

export async function myCourses(): Promise<CourseResponseDTO[]> {
  const { data } = await apiClient.get<CourseResponseDTO[]>('/user/my-courses');
  return data;
}

export async function pendingEnrollments(): Promise<EnrollmentResponseDTO[]> {
  const { data } = await apiClient.get<EnrollmentResponseDTO[]>('/admin/enrollments/pending');
  return data;
}

export async function approveEnrollment(enrollmentId: number): Promise<string> {
  const { data } = await apiClient.put<string>(`/admin/enrollments/${enrollmentId}/approve`);
  return data;
}

export async function rejectEnrollment(enrollmentId: number): Promise<string> {
  const { data } = await apiClient.put<string>(`/admin/enrollments/${enrollmentId}/reject`);
  return data;
}

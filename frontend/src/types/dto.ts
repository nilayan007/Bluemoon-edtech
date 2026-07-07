export type UserRole = 'STUDENT' | 'ADMIN';

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
}

export interface UserResponse {
  id: string;
  name: string;
  email: string;
  role: UserRole;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  id: string;
  name: string;
  email: string;
  verified: boolean;
  accessToken: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface VerifyForgotOtpRequest {
  email: string;
  otp: string;
}

export interface ResetPasswordRequest {
  email: string;
  newPassword: string;
}

export interface AccessTokenResponse {
  accessToken: string;
}

export interface CourseResponseDTO {
  id: number;
  title: string;
  description: string;
  thumbnailUrl: string;
  published: boolean;
}

export interface LessonResponseDTO {
  id: number;
  title: string;
  videoUrl: string;
  orderIndex: number;
}

export interface CreateCourseRequest {
  title: string;
  description?: string;
  thumbnailUrl?: string;
}

export interface UpdateCourseRequest {
  title?: string;
  description?: string;
  thumbnailUrl?: string;
  published?: boolean;
}

export interface AddLessonRequest {
  title: string;
  videoUrl: string;
  orderIndex: number;
}

export interface UpdateLessonRequest {
  title?: string;
  videoUrl?: string;
  orderIndex?: number;
}

export type EnrollmentStatus = 'PENDING' | 'ACTIVE' | 'EXPIRED' | 'REJECTED';

export interface EnrollmentResponseDTO {
  id: number;
  userId: string;
  userName: string;
  userEmail: string;
  courseId: number;
  courseTitle: string;
  status: EnrollmentStatus;
  startDate: string | null;
  expiryDate: string | null;
}

export interface ProfileResponse {
  userPublicId: string;
  college: string | null;
  year: number | null;
  stream: string | null;
  about: string | null;
  address: string | null;
  linkedin: string | null;
  github: string | null;
  website: string | null;
  profileImageUrl: string | null;
}

export interface UpdateProfileRequest {
  college?: string;
  year?: number;
  stream?: string;
  about?: string;
  address?: string;
  linkedin?: string;
  github?: string;
  website?: string;
  profileImageUrl?: string;
}

export interface RequestEmailChangeRequest {
  newEmail: string;
}

export interface VerifyEmailChangeOtpRequest {
  newEmail: string;
  otp: string;
}

export interface ApiError {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
  fieldErrors?: Record<string, string>;
}

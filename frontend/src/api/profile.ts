import { apiClient } from './client';
import type {
  ProfileResponse,
  RequestEmailChangeRequest,
  UpdateProfileRequest,
  VerifyEmailChangeOtpRequest,
} from '../types/dto';

export async function getProfile(): Promise<ProfileResponse> {
  const { data } = await apiClient.get<ProfileResponse>('/api/profile');
  return data;
}

export async function updateProfile(payload: UpdateProfileRequest): Promise<string> {
  const { data } = await apiClient.put<string>('/api/profile', payload);
  return data;
}

export async function requestEmailChange(payload: RequestEmailChangeRequest): Promise<string> {
  const { data } = await apiClient.post<string>('/user/change-email/request', payload);
  return data;
}

export async function verifyEmailChangeOtp(
  payload: VerifyEmailChangeOtpRequest,
): Promise<string> {
  const { data } = await apiClient.post<string>('/user/change-email/verify-otp', payload);
  return data;
}

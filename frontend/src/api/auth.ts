import { apiClient, setAccessToken } from './client';
import type {
  AccessTokenResponse,
  ForgotPasswordRequest,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  ResetPasswordRequest,
  UserResponse,
  VerifyForgotOtpRequest,
} from '../types/dto';

export async function register(payload: RegisterRequest): Promise<UserResponse> {
  const { data } = await apiClient.post<UserResponse>('/api/auth/register', payload);
  return data;
}

export async function login(payload: LoginRequest): Promise<LoginResponse> {
  const { data } = await apiClient.post<LoginResponse>('/api/auth/login', payload);
  setAccessToken(data.accessToken);
  return data;
}

export async function logout(): Promise<void> {
  try {
    await apiClient.post('/api/auth/logout');
  } finally {
    setAccessToken(null);
  }
}

export async function refresh(): Promise<AccessTokenResponse> {
  const { data } = await apiClient.post<AccessTokenResponse>('/api/auth/refresh');
  setAccessToken(data.accessToken);
  return data;
}

export async function forgotPassword(payload: ForgotPasswordRequest): Promise<string> {
  const { data } = await apiClient.post<string>('/api/auth/forgot-password', payload);
  return data;
}

export async function verifyForgotOtp(payload: VerifyForgotOtpRequest): Promise<string> {
  const { data } = await apiClient.post<string>('/api/auth/verify-forgot-otp', payload);
  return data;
}

export async function resetPassword(payload: ResetPasswordRequest): Promise<string> {
  const { data } = await apiClient.post<string>('/api/auth/reset-password', payload);
  return data;
}

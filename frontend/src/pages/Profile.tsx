import { useEffect, useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile, requestEmailChange, updateProfile, verifyEmailChangeOtp } from '../api/profile';
import { extractErrorMessage } from '../api/client';
import type { ProfileResponse, UpdateProfileRequest } from '../types/dto';
import { useAuth } from '../context/AuthContext';
import { ErrorBanner } from '../components/ErrorBanner';
import { LoadingSpinner } from '../components/LoadingSpinner';

const emptyForm: UpdateProfileRequest = {
  college: '',
  year: undefined,
  stream: '',
  about: '',
  address: '',
  linkedin: '',
  github: '',
  website: '',
  profileImageUrl: '',
};

export function Profile() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState<UpdateProfileRequest>(emptyForm);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const [newEmail, setNewEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [emailStep, setEmailStep] = useState<'idle' | 'otp-sent'>('idle');
  const [emailError, setEmailError] = useState<string | null>(null);
  const [emailMessage, setEmailMessage] = useState<string | null>(null);

  useEffect(() => {
    getProfile()
      .then((profile: ProfileResponse) => {
        setForm({
          college: profile.college ?? '',
          year: profile.year ?? undefined,
          stream: profile.stream ?? '',
          about: profile.about ?? '',
          address: profile.address ?? '',
          linkedin: profile.linkedin ?? '',
          github: profile.github ?? '',
          website: profile.website ?? '',
          profileImageUrl: profile.profileImageUrl ?? '',
        });
      })
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setIsLoading(false));
  }, []);

  async function handleSave(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setSuccessMessage(null);
    setIsSaving(true);
    try {
      const message = await updateProfile(form);
      setSuccessMessage(message);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsSaving(false);
    }
  }

  async function handleRequestEmailOtp(e: FormEvent) {
    e.preventDefault();
    setEmailError(null);
    setEmailMessage(null);
    try {
      const message = await requestEmailChange({ newEmail });
      setEmailMessage(message);
      setEmailStep('otp-sent');
    } catch (err) {
      setEmailError(extractErrorMessage(err));
    }
  }

  async function handleVerifyEmailOtp(e: FormEvent) {
    e.preventDefault();
    setEmailError(null);
    try {
      const message = await verifyEmailChangeOtp({ newEmail, otp });
      setEmailMessage(`${message} Redirecting to log in…`);
      await logout();
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setEmailError(extractErrorMessage(err));
    }
  }

  if (isLoading) return <LoadingSpinner />;

  return (
    <div className="mx-auto flex max-w-2xl flex-col gap-10 px-4 py-10">
      <div>
        <h1 className="text-2xl font-semibold text-slate-900">Profile</h1>
        <p className="text-sm text-slate-500">{user?.email}</p>
      </div>

      <form onSubmit={handleSave} className="flex flex-col gap-4">
        <h2 className="font-medium text-slate-900">Profile details</h2>
        <div className="grid grid-cols-2 gap-4">
          <label className="flex flex-col gap-1 text-sm text-slate-700">
            College
            <input
              value={form.college ?? ''}
              onChange={(e) => setForm({ ...form, college: e.target.value })}
              className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
            />
          </label>
          <label className="flex flex-col gap-1 text-sm text-slate-700">
            Year
            <input
              type="number"
              min={1}
              value={form.year ?? ''}
              onChange={(e) =>
                setForm({ ...form, year: e.target.value ? Number(e.target.value) : undefined })
              }
              className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
            />
          </label>
          <label className="flex flex-col gap-1 text-sm text-slate-700">
            Stream
            <input
              value={form.stream ?? ''}
              onChange={(e) => setForm({ ...form, stream: e.target.value })}
              className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
            />
          </label>
          <label className="flex flex-col gap-1 text-sm text-slate-700">
            LinkedIn
            <input
              value={form.linkedin ?? ''}
              onChange={(e) => setForm({ ...form, linkedin: e.target.value })}
              className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
            />
          </label>
          <label className="flex flex-col gap-1 text-sm text-slate-700">
            GitHub
            <input
              value={form.github ?? ''}
              onChange={(e) => setForm({ ...form, github: e.target.value })}
              className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
            />
          </label>
          <label className="flex flex-col gap-1 text-sm text-slate-700">
            Website
            <input
              value={form.website ?? ''}
              onChange={(e) => setForm({ ...form, website: e.target.value })}
              className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
            />
          </label>
        </div>
        <label className="flex flex-col gap-1 text-sm text-slate-700">
          About
          <textarea
            value={form.about ?? ''}
            onChange={(e) => setForm({ ...form, about: e.target.value })}
            rows={3}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
        </label>
        <label className="flex flex-col gap-1 text-sm text-slate-700">
          Address
          <input
            value={form.address ?? ''}
            onChange={(e) => setForm({ ...form, address: e.target.value })}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
        </label>
        {successMessage && <p className="text-sm text-emerald-700">{successMessage}</p>}
        <ErrorBanner message={error} />
        <button
          type="submit"
          disabled={isSaving}
          className="w-fit rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-700 disabled:opacity-50"
        >
          {isSaving ? 'Saving…' : 'Save profile'}
        </button>
      </form>

      <div className="flex flex-col gap-4 border-t border-slate-200 pt-8">
        <h2 className="font-medium text-slate-900">Change email</h2>
        {emailStep === 'idle' ? (
          <form onSubmit={handleRequestEmailOtp} className="flex flex-col gap-3">
            <label className="flex flex-col gap-1 text-sm text-slate-700">
              New email
              <input
                type="email"
                required
                value={newEmail}
                onChange={(e) => setNewEmail(e.target.value)}
                className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
              />
            </label>
            <ErrorBanner message={emailError} />
            <button
              type="submit"
              className="w-fit rounded-md border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
            >
              Send verification code
            </button>
          </form>
        ) : (
          <form onSubmit={handleVerifyEmailOtp} className="flex flex-col gap-3">
            <p className="text-sm text-slate-500">Enter the code sent to {newEmail}.</p>
            <label className="flex flex-col gap-1 text-sm text-slate-700">
              One-time code
              <input
                required
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
              />
            </label>
            <ErrorBanner message={emailError} />
            <button
              type="submit"
              className="w-fit rounded-md border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
            >
              Confirm new email
            </button>
          </form>
        )}
        {emailMessage && <p className="text-sm text-emerald-700">{emailMessage}</p>}
      </div>
    </div>
  );
}

import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { extractErrorMessage, fieldErrorsOf } from '../api/client';
import { ErrorBanner } from '../components/ErrorBanner';

export function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setFieldErrors({});
    setIsSubmitting(true);
    try {
      await register({ name, email, phone, password });
      navigate('/login', { state: { registered: true } });
    } catch (err) {
      setError(extractErrorMessage(err));
      setFieldErrors(fieldErrorsOf(err) ?? {});
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="mx-auto flex max-w-sm flex-col gap-6 px-4 py-16">
      <h1 className="text-2xl font-semibold text-slate-900">Create an account</h1>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <label className="flex flex-col gap-1 text-sm text-slate-700">
          Name
          <input
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          {fieldErrors.name && <span className="text-xs text-red-600">{fieldErrors.name}</span>}
        </label>
        <label className="flex flex-col gap-1 text-sm text-slate-700">
          Email
          <input
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          {fieldErrors.email && <span className="text-xs text-red-600">{fieldErrors.email}</span>}
        </label>
        <label className="flex flex-col gap-1 text-sm text-slate-700">
          Phone
          <input
            required
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          {fieldErrors.phone && <span className="text-xs text-red-600">{fieldErrors.phone}</span>}
        </label>
        <label className="flex flex-col gap-1 text-sm text-slate-700">
          Password
          <input
            type="password"
            required
            minLength={8}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none"
          />
          {fieldErrors.password && (
            <span className="text-xs text-red-600">{fieldErrors.password}</span>
          )}
        </label>
        <ErrorBanner message={error} />
        <button
          type="submit"
          disabled={isSubmitting}
          className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700 disabled:opacity-50"
        >
          {isSubmitting ? 'Creating account…' : 'Sign up'}
        </button>
      </form>
      <p className="text-sm text-slate-500">
        Already have an account?{' '}
        <Link to="/login" className="text-slate-800 underline">
          Log in
        </Link>
      </p>
    </div>
  );
}

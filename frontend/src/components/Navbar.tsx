import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const linkClass = ({ isActive }: { isActive: boolean }) =>
  `text-sm font-medium ${isActive ? 'text-slate-900' : 'text-slate-500 hover:text-slate-800'}`;

export function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  async function handleLogout() {
    await logout();
    navigate('/login');
  }

  return (
    <header className="border-b border-slate-200 bg-white">
      <nav className="mx-auto flex max-w-5xl items-center justify-between px-4 py-3">
        <NavLink to="/" className="text-lg font-semibold text-slate-900">
          Bluemoon Edtech
        </NavLink>
        <div className="flex items-center gap-6">
          <NavLink to="/" className={linkClass} end>
            Courses
          </NavLink>
          {user && (
            <NavLink to="/my-courses" className={linkClass}>
              My Courses
            </NavLink>
          )}
          {user && (
            <NavLink to="/profile" className={linkClass}>
              Profile
            </NavLink>
          )}
          {user?.role === 'ADMIN' && (
            <NavLink to="/admin/courses" className={linkClass}>
              Admin Courses
            </NavLink>
          )}
          {user?.role === 'ADMIN' && (
            <NavLink to="/admin/enrollments" className={linkClass}>
              Enrollments
            </NavLink>
          )}
          {user ? (
            <button
              onClick={handleLogout}
              className="rounded-md bg-slate-900 px-3 py-1.5 text-sm font-medium text-white hover:bg-slate-700"
            >
              Log out
            </button>
          ) : (
            <>
              <NavLink to="/login" className={linkClass}>
                Log in
              </NavLink>
              <NavLink
                to="/register"
                className="rounded-md bg-slate-900 px-3 py-1.5 text-sm font-medium text-white hover:bg-slate-700"
              >
                Sign up
              </NavLink>
            </>
          )}
        </div>
      </nav>
    </header>
  );
}

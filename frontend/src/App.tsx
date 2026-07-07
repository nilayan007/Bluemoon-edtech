import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './routes/ProtectedRoute';
import { AdminRoute } from './routes/AdminRoute';
import { Navbar } from './components/Navbar';
import { Home } from './pages/Home';
import { CourseDetail } from './pages/CourseDetail';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { ForgotPassword } from './pages/ForgotPassword';
import { MyCourses } from './pages/MyCourses';
import { CourseLearn } from './pages/CourseLearn';
import { Profile } from './pages/Profile';
import { AdminCourses } from './pages/admin/AdminCourses';
import { AdminCourseLessons } from './pages/admin/AdminCourseLessons';
import { AdminEnrollments } from './pages/admin/AdminEnrollments';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <div className="min-h-screen bg-slate-50">
          <Navbar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/courses/:courseId" element={<CourseDetail />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />

            <Route element={<ProtectedRoute />}>
              <Route path="/my-courses" element={<MyCourses />} />
              <Route path="/learn/:courseId" element={<CourseLearn />} />
              <Route path="/profile" element={<Profile />} />
            </Route>

            <Route element={<AdminRoute />}>
              <Route path="/admin/courses" element={<AdminCourses />} />
              <Route path="/admin/courses/:courseId/lessons" element={<AdminCourseLessons />} />
              <Route path="/admin/enrollments" element={<AdminEnrollments />} />
            </Route>
          </Routes>
        </div>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;

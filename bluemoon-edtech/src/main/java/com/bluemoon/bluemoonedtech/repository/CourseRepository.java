package com.bluemoon.bluemoonedtech.repository;

import com.bluemoon.bluemoonedtech.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

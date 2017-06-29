'use strict';

const gulp = require('gulp');
const less = require('gulp-less');
const minifyCSS = require('gulp-minify-css');
const plumber = require('gulp-plumber');
const uglify = require('gulp-uglify');
const clean = require('gulp-clean');
const rename = require("gulp-rename");
const fs = require('fs');
const path = require('path');

const lessSrc = 'static/css/less/**/*.less';
const jsSrc = 'static/js/**/*.js';

gulp.task('less', () => {
  return gulp.src(lessSrc)
    .pipe(plumber())
    .pipe(less())
    .pipe(minifyCSS())
    .pipe(rename(stack => {
      stack.extname = '.css';
    }))
    .pipe(gulp.dest('dist/css/'))
});

gulp.task('copy', ['clean'], () => {
  return gulp.src(['static/**/*.*', '!static/css/less/**/*.*'])
    .pipe(gulp.dest('dist/'))
});

gulp.task('js', () => {
  return gulp.src(jsSrc)
    .pipe(uglify())
    .pipe(gulp.dest('dist/js'))
});

gulp.task('auto', () => {
  gulp.watch(lessSrc, ['less'])
});

gulp.task('clean', () => {
  return gulp.src('dist')
    .pipe(clean({force: true}))
});

gulp.task('build', ['copy', 'less']);
gulp.task('dev', ['build', 'auto']);
gulp.task('default', ['build',  'js']);
var gulp = require('gulp');
var uglify = require('gulp-uglify');
var sass = require('gulp-sass');
var concat = require('gulp-concat');
var autoprefixer = require('gulp-autoprefixer');
var minifyCSS = require('gulp-minify-css');
var rename = require('gulp-rename');
var browserify = require('browserify');
var babelify = require('babelify');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var livereload = require('gulp-livereload');
var plumber = require('gulp-plumber');
var imagemin = require('gulp-imagemin');

gulp.task('build-js', function () {
  browserify({
    entries: './app/assets/javascripts/main.js',
    extensions: ['.js', 'jsx'],
    debug: true
  })
  .transform("babelify",
    {
      presets: ["es2015", "react", "stage-0"]
    })
  .bundle()
  .on('error', function(err) {
    console.log(err.message + "\n\n === END OF STACK TRACE === \n\n")
  })
  .pipe(source('bundle.js'))
  .pipe(rename('zencroissants.js'))
  .pipe(gulp.dest('./public/compiled/'))
  .pipe(buffer())
  .pipe(uglify())
  .pipe(rename('zencroissants.min.js'))
  .pipe(gulp.dest('./public/compiled/'));
});

gulp.task('watch-js', function() {
    gulp.watch(['./app/assets/javascripts/**/*.js', './app/assets/javascripts/**/*.jsx'], ['build-js']);
});

gulp.task('build-sass', function () {
    gulp.src('./app/assets/stylesheets/**/*.sass')
      .pipe(plumber())
      .pipe(sass({outputStyle: 'compressed'}).on('error', sass.logError))
      .pipe(plumber.stop())
      .pipe(autoprefixer({
        browsers: [
            '> 1%',
            'last 2 versions',
            'firefox >= 4',
            'safari 7',
            'safari 8',
            'IE 8',
            'IE 9',
            'IE 10',
            'IE 11'
        ],
        cascade: false
    }))
    .pipe(gulp.dest('./public/compiled/'))
    .pipe(livereload())
    .pipe(minifyCSS())
    .pipe(rename({
        suffix: '.min'
    }))
    .pipe(gulp.dest('./public/compiled/'))
});

gulp.task('watch-sass', function() {
    livereload.listen();
    gulp.watch(['./app/assets/stylesheets/**/*.sass'], ['build-sass']);
});

gulp.task('imagemin', function () {
    return gulp.src('./public/img/*')
        .pipe(imagemin())
        .pipe(gulp.dest('./public/img/min/'));
});

gulp.task('build', ['build-sass', 'build-js']);

gulp.task('default', ['build-sass', 'build-js', 'watch-sass', 'watch-js']);

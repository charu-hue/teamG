<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（科目）</h2>

      <!-- 🔍 検索フォーム -->
      <form method="get" id="search-form">
        <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">

          <div class="col-2 text-left"><label>科目情報</label></div>

          <div class="col-2">
            <label class="form-label" for="student-f1-select">入学年度</label>
            <select class="form-select form-select-sm" id="student-f1-select" name="f1">
              <option value="0">--------</option>
              <c:forEach var="year" items="${ent_year_set}">
                <option value="${year}" <c:if test="${year==f1}">selected</c:if>>${year}</option>
              </c:forEach>
            </select>
          </div>

          <div class="col-2">
            <label class="form-label" for="student-f2-select">クラス</label>
            <select class="form-select form-select-sm" id="student-f2-select" name="f2">
              <option value="0">-------</option>
              <c:forEach var="num" items="${class_num_set}">
                <option value="${num}" <c:if test="${num==f2}">selected</c:if>>${num}</option>
              </c:forEach>
            </select>
          </div>

          <div class="col-4">
            <label class="form-label" for="student-f3-select">科目名</label>
            <select class="form-select" id="student-f3-select" name="f3">
              <option value="0">-------</option>
              <c:forEach var="subject" items="${subject_name_set}">
                <option value="${subject}" <c:if test="${subject==f3}">selected</c:if>>${subject}</option>
              </c:forEach>
            </select>
          </div>

          <div class="col-2 text-center">
            <button type="submit" class="btn btn-secondary" onclick="return handleSubmit()">検索</button>
          </div>

          <div class="col-2 text-left mt-3"><label>学生情報</label></div>

          <div class="col-4 mt-3">
            <label class="form-label" for="student-f4-select">学生番号</label>
            <input type="text" class="form-control" placeholder="学生番号を入力してください" name="f4" value="${no}">
          </div>

          <div class="col-2 text-center mt-3">
            <button type="submit" class="btn btn-secondary" onclick="return handleSubmit()">検索</button>
          </div>

          <div class="mt-2 text-warning">${errors.get("f1")}</div>
        </div>
      </form>

      <p style="color: #00bfff; font-size: 0.9rem;">
        科目情報または学生番号のいずれかを入力して検索ボタンをクリックしてください。
      </p>

      <!-- 📊 検索結果表示 -->
      <div>件数: ${fn:length(list)}</div>
      <table class="table table-hover">
        <thead>
          <tr>
            <th>入学年度</th>
            <th>クラス</th>
            <th>学生番号</th>
            <th>氏名</th>
            <th>1回</th>
            <th>2回</th>
            <th>3回</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="item" items="${list}">
            <tr>
              <td>${item.entYear}</td>
              <td>${item.classNum}</td>
              <td>${item.studentNo}</td>
              <td>${item.name}</td>
              <td>${item.points[1]}</td>
              <td>${item.points[2]}</td>
              <td>${item.points[3]}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <!-- 🔁 検索振り分けJS -->
      <script>
        function handleSubmit() {
          const form = document.getElementById('search-form');
          const f1 = document.getElementById('student-f1-select').value;
          const f2 = document.getElementById('student-f2-select').value;
          const f3 = document.getElementById('student-f3-select').value;
          const f4 = document.querySelector('input[name="f4"]').value.trim();

          const hasStudent = f4 !== "";
          const hasSubject = f1 !== "0" && f2 !== "0" && f3 !== "0";

          if (hasStudent && !hasSubject) {
            form.action = "TestListStudentExecuteAction";
          } else if (!hasStudent && hasSubject) {
            form.action = "TestListSubjectExecuteAction";
          } else if (hasStudent && hasSubject) {
            form.action = "TestListStudentExecuteAction"; // 学生検索優先
          } else {
            alert("科目情報または学生番号のいずれかを入力してください。");
            return false;
          }

          return true;
        }
      </script>
    </section>
  </c:param>
</c:import>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績一覧（学生）</h2>

      <!-- 🔍 検索フォーム -->

          <div class="col-2 text-left"><label>学生情報</label></div>



      <!-- 📊 検索結果 -->
      <c:if test="${not empty students}">
        <div>件数: ${fn:length(students)}</div>
        <table class="table table-hover">
          <thead>
            <tr>
              <th>科目名</th>
              <th>科目コード</th>
              <th>回数</th>
              <th>点数</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="item" items="${students}">
              <tr>
                <td>${item.subjectName}</td>
                <td>${item.subjectCd}</td>
                <td>${item.num}</td>
                <td>${item.point}</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:if>

      <c:if test="${empty students && empty error && empty message}">
        <div class="text-muted">成績情報がまだ表示されていません。</div>
      </c:if>

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
            form.action = "TestListStudentExecuteAction";
          } else {
            alert("科目情報または学生番号のいずれかを入力してください。");
            return false;
          }

          return true;
        }
      </script>
            <a href="TestList.action">検索画面へ戻る</a>
    </section>
  </c:param>
</c:import>

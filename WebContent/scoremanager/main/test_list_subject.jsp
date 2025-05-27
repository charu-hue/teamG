<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（科目）</h2>

      <!-- 🔍 検索フォーム -->


          <div class="col-2 text-left"><label>科目情報</label></div>



      <!-- 📊 検索結果表示 -->
      <c:if test="${not empty students}">
        <div>件数: ${fn:length(students)}</div>
        <table class="table table-hover">
          <thead>
            <tr>
              <th>入学年度</th>
              <th>クラス</th>
              <th>学生番号</th>
              <th>氏名</th>
              <th>1回</th>
              <th>2回</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="item" items="${students}">
              <tr>
                <td>${item.entYear}</td>
                <td>${item.classNum}</td>
                <td>${item.studentNo}</td>
                <td>${item.studentName}</td>
                <td>${item.points["1"]}</td>
				<td>${item.points["2"]}</td>
              </tr>

            </c:forEach>
          </tbody>
        </table>
      </c:if>


      <c:if test="${empty students}">
        <div class="text-danger">検索結果はありませんでした。</div>
      </c:if>

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
      <a href="TestList.action">検索画面へ戻る</a>
    </section>
  </c:param>
</c:import>

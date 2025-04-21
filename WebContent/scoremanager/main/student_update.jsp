<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
<c:param name="title">
        得点管理システム
</c:param>
<c:param name="scripts"></c:param>
<c:param name="content">
<section class="me=4">
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報変更</h2>
<form action="StudentUpdateExecute.action" method="get">
<div class="mx-3 mb-3 py-2 rounded" id="registrationForm">
<div class="mb-3">
    <label class="form-label" for="student-f1-input">入学年度</label>
    <input type="text" class="form-control" id="student-f1-input" name="f1"
           value="${f1}" readonly>
</div>

<p></p>
<div class="mb-3">
<label class="form-label" for="student-f2-select">学生番号</label>
<input type="text" class=" form-control" placeholder="学生番号を入力してください" name="f2" required value="${no }">
</div>
<p></p>
<div class="mb-3">
<label class="form-label" for="student-f3-select">氏名</label>
<input type="text" class=" form-control" placeholder="氏名を入力してください" name="f3" required value="${name }">
</div>
<p></p>
<div class="mb-3">
<label class="form-label" for="student-f4-select">クラス</label>
<select class="form-select" id="student-f4-select" name="f4">
<option value="0">-------</option>
<c:forEach var="num" items="${class_num_set}">
<%--現在のnumと選択されたf2が一致されていた場合selectedを追記 --%>
<option value="${num}"<c:if test="{num == f4}">selected</c:if>>${num}</option>
</c:forEach>
</select>
</div>
<div class="mb-3">
<label class="form-check-label" for="student-f3-check">在学中
<%-- パラメーターf3が存在している場合checkedを追記 --%>
<input class="form-check-input" type="checkbox"
                                   id="student-f5-check" name="f5" value="true"
<c:if test="${isAttend}">checked</c:if>>
</label>
</div>
<p></p>
<div class="mb-3 text-left">
<button class="btn btn-secondary" id="filter-button">変更</button>
</div>
<div class="mt-2 text-warning">${errors.get("f1")}</div>
</div>
</form>
<a href="StudentList.action">戻る</a>
</section>
</c:param>
</c:import>
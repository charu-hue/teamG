<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>
            <form action="StudentCreateExecute.action" method="get">
                <div class="mx-3 mb-3 py-2 rounded" id="registrationForm">

                    <!-- 入学年度 -->
                    <div class="mb-3">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="f1">
                            <option value="0">-------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${param.f1 == year}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                        <div class="text-danger">${errors.f1}</div>
                    </div>

                    <!-- 学生番号 -->
                    <div class="mb-3">
                        <label class="form-label" for="student-f2-input">学生番号</label>
                        <input type="text" class="form-control" id="student-f2-input" name="f2"
                               placeholder="学生番号を入力してください" required value="${no}">
                        <div class="text-danger">${errors.f2}</div>
                    </div>

                    <!-- 氏名 -->
                    <div class="mb-3">
                        <label class="form-label" for="student-f3-input">氏名</label>
                        <input type="text" class="form-control" id="student-f3-input" name="f3"
                               placeholder="氏名を入力してください" required value="${name}">
                        <div class="text-danger">${errors.f3}</div>
                    </div>

                    <!-- クラス -->
                    <div class="mb-3">
                        <label class="form-label" for="student-f4-select">クラス</label>
                        <select class="form-select" id="student-f4-select" name="f4">
                            <option value="0">-------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${param.f4 == num}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                        <div class="text-danger">${errors.f4}</div>
                    </div>

                    <!-- 登録ボタン -->
                    <div class="mb-3 text-left">
                        <button class="btn btn-secondary" id="filter-button">登録して終了</button>
                    </div>
                </div>
            </form>

            <a href="StudentList.action">戻る</a>
        </section>
    </c:param>
</c:import>

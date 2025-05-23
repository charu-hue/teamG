<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">科目管理システム - 登録完了</c:param>
    <c:param name="scripts"></c:param>


    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報登録</h2>

            <div class="alert alert-success" role="alert">
                科目の登録が正常に完了しました。
            </div>

            <div class="mt-3">
                <button class="btn btn-secondary" onclick="history.back()">戻る</button>
                <a href="SubjectList.action" class="btn btn-primary ms-2">科目一覧へ</a>
            </div>
        </section>
    </c:param>
</c:import>

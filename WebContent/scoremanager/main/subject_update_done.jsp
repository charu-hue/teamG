<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
<c:param name="title">得点管理システム - 変更完了</c:param>
<c:param name="scripts"></c:param>

<c:param name="content">
<section class="no">
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報登録</h2>

        <div class="alert alert-success" role="alert">
            更新が完了しました。
</div>
        <div>
<button class="btn btn-secondary" onclick="history.back()">戻る</button>
<a href="SubjectList.action" class="btn btn-primary ms-2">科目一覧</a>
</div>
</section>
</c:param>
</c:import>
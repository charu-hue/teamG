<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2>
            <form action="SubjectDeleteExecute.action" method="post" class="px-4">
                <p>「${name}（${cd}）」を削除してもよろしいですか？</p>

                <!-- 削除ボタン -->
                <button type="submit" class="btn btn-danger">削除</button>

                <!-- 戻るリンク -->
                <div class="mt-3">
                    <a href="SubjectList.action">戻る</a>
                </div>

                <!-- 削除対象の科目コードをhiddenで送る -->
                <input type="hidden" name="code" value="${subject.code}">
            </form>
        </section>
    </c:param>
</c:import>


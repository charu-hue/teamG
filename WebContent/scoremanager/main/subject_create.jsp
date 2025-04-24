<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">
        得点管理システム
    </c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報登録</h2>

            <!-- 科目作成のアクションに向けたフォーム -->
            <form action="SubjectCreateExecute.action" method="get">
                <div class="mx-3 mb-3 py-2 rounded" id="registrationForm">

                    <!-- 科目コード入力 -->
                    <div class="mb-3">
                        <label class="form-label" for="subject-f1">科目コード</label>
                        <input type="text" id="subject-f1" class="form-control" placeholder="科目コードを入力してください" name="f1" required value="${cd}">
                    </div>

                    <!-- 科目名入力 -->
                    <div class="mb-3">
                        <label class="form-label" for="subject-f2">科目名</label>
                        <input type="text" id="subject-f2" class="form-control" placeholder="科目名を入力してください" name="f2" required value="${name}">
                    </div>

                    <!-- 登録ボタン -->
                    <div class="mb-3 text-left">
                        <button type="submit" class="btn btn-secondary">登録</button>
                    </div>

                    <!-- エラーメッセージ表示 -->
                    <div class="mt-2 text-warning">${errors.get("f1")}</div>
                </div>
            </form>

            <!-- 戻るリンク（科目一覧へ） -->
            <a href="SubjectList.action">戻る</a>
        </section>
    </c:param>
</c:import>

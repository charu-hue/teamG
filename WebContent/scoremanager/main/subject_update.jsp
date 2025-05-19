<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム - 科目管理</c:param>
    <c:param name="scripts"></c:param>

    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>

            <form action="SubjectUpdateExecute.action" method="post">
                <div class="mx-3 mb-3 py-2 rounded" id="registrationForm">
                    <div class="mb-3">
                        <label class="form-label" for="subject-f1-input">科目コード</label>
                        <input type="text" class="form-control" id="subject-f1-input"
                               name="f1" value="${f1}" readonly>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="subject-f2-input">科目名</label>
                        <input type="text" class="form-control" id="subject-f2-input"
                               name="f2" value="${f2}" required
                               value="${subject.name}">
                    </div>

                    <div class="mb-3 text-left">
                        <button class="btn btn-secondary" type="submit">変更</button>
                    </div>

                    <div class="mt-2 text-warning">${errors.get("f1")}</div>
                </div>
            </form>

            <a href="SubjectList.action" class="btn btn-link mt-3">戻る</a>
        </section>
    </c:param>
</c:import>

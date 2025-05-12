<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

            <!-- 検索フォーム -->
            <form method="get">
                <div class="row px-4 mb-3 align-items-end">
                    <div class="col-md-3">
                        <label for="entYear" class="form-label">入学年度</label>
                        <select class="form-select" id="entYear" name="entYear">
                            <c:forEach var="year" items="${entYearList}">
                                <option value="${year}" <c:if test="${year == entYear}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label for="classNum" class="form-label">クラス</label>
                        <select class="form-select" id="classNum" name="classNum">
                            <c:forEach var="cls" items="${classNumList}">
                                <option value="${cls}" <c:if test="${cls == classNum}">selected</c:if>>${cls}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-4">
                        <label for="subjectCd" class="form-label">科目</label>
                        <select class="form-select" id="subjectCd" name="subject">
                            <c:forEach var="subj" items="${subjectList}">
                                <option value="${subj.cd}" <c:if test="${subj.cd == subjectCd}">selected</c:if>>${subj.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label for="no" class="form-label">回数</label>
                        <select class="form-select" id="no" name="no">
                            <c:forEach var="i" begin="1" end="10">
                                <option value="${i}" <c:if test="${i == no}">selected</c:if>>${i}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-1 text-end">
                        <button type="submit" class="btn btn-secondary">検索</button>
                    </div>
                </div>
            </form>

            <!-- 得点登録フォーム（検索後に表示） -->
            <c:if test="${not empty testList}">
                <form method="post" action="TestRegistExecute.action">
                    <!-- 検索条件保持 -->
                    <input type="hidden" name="entYear" value="${entYear}" />
                    <input type="hidden" name="classNum" value="${classNum}" />
                    <input type="hidden" name="subjectCd" value="${subjectCd}" />
                    <input type="hidden" name="no" value="${no}" />

                    <div class="mt-4 mb-2 fw-bold">科目: ${subjectName}（第${no}回）</div>

                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>入学年度</th>
                                <th>クラス</th>
                                <th>学生番号</th>
                                <th>氏名</th>
                                <th>点数</th>
                                <th>エラー</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="test" items="${testList}">
                                <tr>
                                    <td>${test.student.entYear}</td>
                                    <td>${test.classNum}</td>
                                    <td>
                                        ${test.student.no}
                                        <input type="hidden" name="studentNo" value="${test.student.no}" />
                                    </td>
                                    <td>${test.student.name}</td>
                                    <td>
                                        <input type="number" name="point" value="${test.point}" class="form-control" />
                                    </td>
                                    <td class="text-danger">
                                        <c:if test="${not empty errorMessages[test.student.no]}">
                                            ${errorMessages[test.student.no]}
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-secondary">登録して終了</button>
                    </div>
                </form>
            </c:if>

            <!-- メッセージ表示 -->
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
        </section>
    </c:param>
</c:import>

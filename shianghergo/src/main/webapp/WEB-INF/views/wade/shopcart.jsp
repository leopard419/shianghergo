<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
	<script src="http://code.jquery.com/jquery-1.12.4.min.js" ></script>
  <meta charset="UTF-8">
  <title>Document</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/static/css/main.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/layui/css/layui.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/res/layui/layui.js"></script>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
</head>
<body>

<!--   <div class="header"> -->
<!--     <div class="headerLayout w1200"> -->
<!--       <div class="headerCon"> -->
<!--         <h1 class="mallLogo"> -->
<!--           <a href="index.jsp"> -->
<%--             <img src="${pageContext.request.contextPath}/res/static/img/logo4.png" width="100" height="100"> --%>
<!--           </a> -->
<!--         </h1> -->
<!--         <div class="mallSearch"> -->
<!--           <form action="" class="layui-form" novalidate> -->
<!--             <input type="text" name="title" required  lay-verify="required" autocomplete="off" class="layui-input" placeholder="请输入需要的商品"> -->
<!--             <button class="layui-btn" lay-submit lay-filter="formDemo"> -->
<!--                 <i class="layui-icon layui-icon-search"></i> -->
<!--             </button> -->
<!--             <input type="hidden" name="" value=""> -->
<!--           </form> -->
<!--         </div> -->
<!--       </div> -->
<!--     </div> -->
<!--   </div> -->


  <div class="content content-nav-base shopcart-content">
    <div class="main-nav">
      <div class="inner-cont0">
        <div class="inner-cont1 w1200">
<!--           <div class="inner-cont2"> -->
<%--             <a href="${pageContext.request.contextPath}/html/commodity.jsp" class="active">所有商品</a> --%>
<!--             <a href="buytoday.jsp">今日团购</a> -->
<!--             <a href="about.jsp">关于我们</a> -->
<!--           </div> -->
        </div>
      </div>
    </div>
<!--     <div class="banner-bg w1200"> -->
<!--       <h3>夏季清仓</h3> -->
<!--       <p>宝宝被子、宝宝衣服3折起</p> -->
<!--     </div> -->
    <div class="cart w1200">
      <div class="cart-table-th">
        <div class="th th-chk">
          <div class="select-all">
            <div class="cart-checkbox">
              <input class="check-all check" id="allCheckked" type="checkbox" value="true">
            </div>
          		<label>&nbsp;&nbsp;全選</label>
          </div>
        </div>
        <div class="th th-item">
          <div class="th-inner">
            	商品
          </div>
        </div>
        <div class="th th-price">
          <div class="th-inner">
            	單價
          </div>
        </div>
        <div class="th th-amount">
          <div class="th-inner">
            	數量
          </div>
        </div>
        <div class="th th-sum">
          <div class="th-inner">
            	小計
          </div>
        </div>
        <div class="th th-op">
          <div class="th-inner">
            	操作
          </div>
        </div>  
      </div>
      <div class="OrderList">
        <div class="order-content" id="list-cont">
        <c:forEach var="item" items="${cartitems}">
          <ul class="item-content layui-clear">
            <li class="th th-chk">
              <div class="select-all">
                <div class="cart-checkbox">
                  <input class="CheckBoxShop check" id="" type="checkbox" num="all" name="select-all" value="true">
                </div>
              </div>
            </li>
            <li class="th th-item">
              <div class="item-cont">
                <a href="javascript:;"><img src="${pageContext.request.contextPath}/res/static/img/paging_img1.jpg" alt=""></a>
                <div class="text">
                  <div class="title">${item.name}</div>
<!--                  <p><span>粉色</span>  <span>130</span>cm</p> -->
                </div>
              </div>
            </li>
            <li class="th th-price">
              <span class="th-su">${item.price}</span>
            </li>
            <li class="th th-amount">
              <div class="box-btn layui-clear">
                <div class="less layui-btn">-</div>
                <input class="Quantity-input" type="" name="" value="${item.amount}" disabled="disabled">
                <div class="add layui-btn">+</div>
              </div>
            </li>
            <li class="th th-sum">
              <span class="sum" id="${item.id}a"></span>
              <script>
				var a = ${item.price} * ${item.amount};
				$("#"+${item.id}+"a").text(a);
			</script>
            </li>
            <li class="th th-op">
              <span class="dele-btn">刪除</span>
            </li>
          </ul>
        </c:forEach>      

      <!-- 模版导入数据 -->
      <!-- <script type="text/html" id="demo">
        {{# layui.each(d.infoList,function(index,item){}}
          <ul class="item-content layui-clear">
            <li class="th th-chk">
              <div class="select-all">
                <div class="cart-checkbox">
                  <input class="CheckBoxShop check" id="" type="checkbox" num="all" name="select-all" value="true">
                </div>
              </div>
            </li>
            <li class="th th-item">
              <div class="item-cont">
                <a href="javascript:;"><img src="../res/static/img/paging_img1.jpg" alt=""></a>
                <div class="text">
                  <div class="title">宝宝T恤棉质小衫</div>
                  <p><span>粉色</span>  <span>130</span>cm</p>
                </div>
              </div>
            </li>
            <li class="th th-price">
              <span class="th-su">189.00</span>
            </li>
            <li class="th th-amount">
              <div class="box-btn layui-clear">
                <div class="less layui-btn">-</div>
                <input class="Quantity-input" type="" name="" value="1" disabled="disabled">
                <div class="add layui-btn">+</div>
              </div>
            </li>
            <li class="th th-sum">
              <span class="sum">189.00</span>
            </li>
            <li class="th th-op">
              <span class="dele-btn">删除</span>
            </li>
          </ul>
        {{# });}}
      </script> -->


      <div class="FloatBarHolder layui-clear">
        <div class="th th-chk">
          <div class="select-all">
            <div class="cart-checkbox">
              <input class="check-all check" id="" name="select-all" type="checkbox"  value="true">
            </div>
            <label>&nbsp;&nbsp;已选<span class="Selected-pieces">0</span>件</label>
          </div>
        </div>
        <div class="th batch-deletion">
          <span class="batch-dele-btn">批量删除</span>
        </div>
        <div class="th Settlement">
          <button class="layui-btn">结算</button>
        </div>
        <div class="th total">
          <p>应付：<span class="pieces-total">0</span></p>
        </div>
      </div>
    </div>
  </div>

<script type="text/javascript">
  layui.config({
    base: '../res/static/js/util/' //你存放新模块的目录，注意，不是layui的模块目录
  }).use(['mm','jquery','element','car'],function(){
    var mm = layui.mm,$ = layui.$,element = layui.element,car = layui.car;
    
    // 模版导入数据
    // var html = demo.innerHTML,
    // listCont = document.getElementById('list-cont');
    // mm.request({
    //   url: '../json/shopcart.json',
    //   success : function(res){
    //     listCont.innerHTML = mm.renderHtml(html,res)
    //     element.render();
    //     car.init()
    //   },
    //   error: function(res){
    //     console.log(res);
    //   }
    // })
    // 
    car.init()


});
</script>
</body>
</html>
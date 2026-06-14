<template>
  <div>
    <div class="jumbotron">
      <h1>电商购物平台</h1>
      <p>浏览商品、加入购物车、下单购买，支持管理员后台管理。</p>
      <p>
        <router-link to="/products" class="btn btn-primary btn-lg">浏览商品</router-link>
      </p>
    </div>
    <div class="row">
      <div class="col-md-4">
        <h4>商品浏览</h4>
        <p>按分类和名称搜索商品，查看详情和用户评价。</p>
      </div>
      <div class="col-md-4">
        <h4>购物车下单</h4>
        <p>加入购物车后可提交订单，自动扣减库存。</p>
      </div>
      <div class="col-md-4">
        <h4>订单管理</h4>
        <p>查看历史订单，对已收货商品进行评价。</p>
      </div>
    </div>
  </div>
</template>

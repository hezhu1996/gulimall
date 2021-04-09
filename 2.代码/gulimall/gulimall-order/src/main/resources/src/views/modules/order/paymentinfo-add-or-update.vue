<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="¶©µ¥ºÅ£¨¶ÔÍâÒµÎñºÅ£©" prop="orderSn">
      <el-input v-model="dataForm.orderSn" placeholder="¶©µ¥ºÅ£¨¶ÔÍâÒµÎñºÅ£©"></el-input>
    </el-form-item>
    <el-form-item label="¶©µ¥id" prop="orderId">
      <el-input v-model="dataForm.orderId" placeholder="¶©µ¥id"></el-input>
    </el-form-item>
    <el-form-item label="Ö§¸¶±¦½»Ò×Á÷Ë®ºÅ" prop="alipayTradeNo">
      <el-input v-model="dataForm.alipayTradeNo" placeholder="Ö§¸¶±¦½»Ò×Á÷Ë®ºÅ"></el-input>
    </el-form-item>
    <el-form-item label="Ö§¸¶×Ü½ð¶î" prop="totalAmount">
      <el-input v-model="dataForm.totalAmount" placeholder="Ö§¸¶×Ü½ð¶î"></el-input>
    </el-form-item>
    <el-form-item label="½»Ò×ÄÚÈÝ" prop="subject">
      <el-input v-model="dataForm.subject" placeholder="½»Ò×ÄÚÈÝ"></el-input>
    </el-form-item>
    <el-form-item label="Ö§¸¶×´Ì¬" prop="paymentStatus">
      <el-input v-model="dataForm.paymentStatus" placeholder="Ö§¸¶×´Ì¬"></el-input>
    </el-form-item>
    <el-form-item label="´´½¨Ê±¼ä" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="´´½¨Ê±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="È·ÈÏÊ±¼ä" prop="confirmTime">
      <el-input v-model="dataForm.confirmTime" placeholder="È·ÈÏÊ±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="»Øµ÷ÄÚÈÝ" prop="callbackContent">
      <el-input v-model="dataForm.callbackContent" placeholder="»Øµ÷ÄÚÈÝ"></el-input>
    </el-form-item>
    <el-form-item label="»Øµ÷Ê±¼ä" prop="callbackTime">
      <el-input v-model="dataForm.callbackTime" placeholder="»Øµ÷Ê±¼ä"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          orderSn: '',
          orderId: '',
          alipayTradeNo: '',
          totalAmount: '',
          subject: '',
          paymentStatus: '',
          createTime: '',
          confirmTime: '',
          callbackContent: '',
          callbackTime: ''
        },
        dataRule: {
          orderSn: [
            { required: true, message: '¶©µ¥ºÅ£¨¶ÔÍâÒµÎñºÅ£©不能为空', trigger: 'blur' }
          ],
          orderId: [
            { required: true, message: '¶©µ¥id不能为空', trigger: 'blur' }
          ],
          alipayTradeNo: [
            { required: true, message: 'Ö§¸¶±¦½»Ò×Á÷Ë®ºÅ不能为空', trigger: 'blur' }
          ],
          totalAmount: [
            { required: true, message: 'Ö§¸¶×Ü½ð¶î不能为空', trigger: 'blur' }
          ],
          subject: [
            { required: true, message: '½»Ò×ÄÚÈÝ不能为空', trigger: 'blur' }
          ],
          paymentStatus: [
            { required: true, message: 'Ö§¸¶×´Ì¬不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '´´½¨Ê±¼ä不能为空', trigger: 'blur' }
          ],
          confirmTime: [
            { required: true, message: 'È·ÈÏÊ±¼ä不能为空', trigger: 'blur' }
          ],
          callbackContent: [
            { required: true, message: '»Øµ÷ÄÚÈÝ不能为空', trigger: 'blur' }
          ],
          callbackTime: [
            { required: true, message: '»Øµ÷Ê±¼ä不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/order/paymentinfo/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.orderSn = data.paymentInfo.orderSn
                this.dataForm.orderId = data.paymentInfo.orderId
                this.dataForm.alipayTradeNo = data.paymentInfo.alipayTradeNo
                this.dataForm.totalAmount = data.paymentInfo.totalAmount
                this.dataForm.subject = data.paymentInfo.subject
                this.dataForm.paymentStatus = data.paymentInfo.paymentStatus
                this.dataForm.createTime = data.paymentInfo.createTime
                this.dataForm.confirmTime = data.paymentInfo.confirmTime
                this.dataForm.callbackContent = data.paymentInfo.callbackContent
                this.dataForm.callbackTime = data.paymentInfo.callbackTime
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/order/paymentinfo/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'orderSn': this.dataForm.orderSn,
                'orderId': this.dataForm.orderId,
                'alipayTradeNo': this.dataForm.alipayTradeNo,
                'totalAmount': this.dataForm.totalAmount,
                'subject': this.dataForm.subject,
                'paymentStatus': this.dataForm.paymentStatus,
                'createTime': this.dataForm.createTime,
                'confirmTime': this.dataForm.confirmTime,
                'callbackContent': this.dataForm.callbackContent,
                'callbackTime': this.dataForm.callbackTime
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>

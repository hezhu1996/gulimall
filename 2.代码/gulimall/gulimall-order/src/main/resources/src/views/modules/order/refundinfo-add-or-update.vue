<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="ÍË¿îµÄ¶©µ¥" prop="orderReturnId">
      <el-input v-model="dataForm.orderReturnId" placeholder="ÍË¿îµÄ¶©µ¥"></el-input>
    </el-form-item>
    <el-form-item label="ÍË¿î½ð¶î" prop="refund">
      <el-input v-model="dataForm.refund" placeholder="ÍË¿î½ð¶î"></el-input>
    </el-form-item>
    <el-form-item label="ÍË¿î½»Ò×Á÷Ë®ºÅ" prop="refundSn">
      <el-input v-model="dataForm.refundSn" placeholder="ÍË¿î½»Ò×Á÷Ë®ºÅ"></el-input>
    </el-form-item>
    <el-form-item label="ÍË¿î×´Ì¬" prop="refundStatus">
      <el-input v-model="dataForm.refundStatus" placeholder="ÍË¿î×´Ì¬"></el-input>
    </el-form-item>
    <el-form-item label="ÍË¿îÇþµÀ[1-Ö§¸¶±¦£¬2-Î¢ÐÅ£¬3-ÒøÁª£¬4-»ã¿î]" prop="refundChannel">
      <el-input v-model="dataForm.refundChannel" placeholder="ÍË¿îÇþµÀ[1-Ö§¸¶±¦£¬2-Î¢ÐÅ£¬3-ÒøÁª£¬4-»ã¿î]"></el-input>
    </el-form-item>
    <el-form-item label="" prop="refundContent">
      <el-input v-model="dataForm.refundContent" placeholder=""></el-input>
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
          orderReturnId: '',
          refund: '',
          refundSn: '',
          refundStatus: '',
          refundChannel: '',
          refundContent: ''
        },
        dataRule: {
          orderReturnId: [
            { required: true, message: 'ÍË¿îµÄ¶©µ¥不能为空', trigger: 'blur' }
          ],
          refund: [
            { required: true, message: 'ÍË¿î½ð¶î不能为空', trigger: 'blur' }
          ],
          refundSn: [
            { required: true, message: 'ÍË¿î½»Ò×Á÷Ë®ºÅ不能为空', trigger: 'blur' }
          ],
          refundStatus: [
            { required: true, message: 'ÍË¿î×´Ì¬不能为空', trigger: 'blur' }
          ],
          refundChannel: [
            { required: true, message: 'ÍË¿îÇþµÀ[1-Ö§¸¶±¦£¬2-Î¢ÐÅ£¬3-ÒøÁª£¬4-»ã¿î]不能为空', trigger: 'blur' }
          ],
          refundContent: [
            { required: true, message: '不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/order/refundinfo/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.orderReturnId = data.refundInfo.orderReturnId
                this.dataForm.refund = data.refundInfo.refund
                this.dataForm.refundSn = data.refundInfo.refundSn
                this.dataForm.refundStatus = data.refundInfo.refundStatus
                this.dataForm.refundChannel = data.refundInfo.refundChannel
                this.dataForm.refundContent = data.refundInfo.refundContent
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
              url: this.$http.adornUrl(`/order/refundinfo/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'orderReturnId': this.dataForm.orderReturnId,
                'refund': this.dataForm.refund,
                'refundSn': this.dataForm.refundSn,
                'refundStatus': this.dataForm.refundStatus,
                'refundChannel': this.dataForm.refundChannel,
                'refundContent': this.dataForm.refundContent
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

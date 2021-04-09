<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="¶©µ¥id" prop="orderId">
      <el-input v-model="dataForm.orderId" placeholder="¶©µ¥id"></el-input>
    </el-form-item>
    <el-form-item label="²Ù×÷ÈË[ÓÃ»§£»ÏµÍ³£»ºóÌ¨¹ÜÀíÔ±]" prop="operateMan">
      <el-input v-model="dataForm.operateMan" placeholder="²Ù×÷ÈË[ÓÃ»§£»ÏµÍ³£»ºóÌ¨¹ÜÀíÔ±]"></el-input>
    </el-form-item>
    <el-form-item label="²Ù×÷Ê±¼ä" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="²Ù×÷Ê±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="¶©µ¥×´Ì¬¡¾0->´ý¸¶¿î£»1->´ý·¢»õ£»2->ÒÑ·¢»õ£»3->ÒÑÍê³É£»4->ÒÑ¹Ø±Õ£»5->ÎÞÐ§¶©µ¥¡¿" prop="orderStatus">
      <el-input v-model="dataForm.orderStatus" placeholder="¶©µ¥×´Ì¬¡¾0->´ý¸¶¿î£»1->´ý·¢»õ£»2->ÒÑ·¢»õ£»3->ÒÑÍê³É£»4->ÒÑ¹Ø±Õ£»5->ÎÞÐ§¶©µ¥¡¿"></el-input>
    </el-form-item>
    <el-form-item label="±¸×¢" prop="note">
      <el-input v-model="dataForm.note" placeholder="±¸×¢"></el-input>
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
          orderId: '',
          operateMan: '',
          createTime: '',
          orderStatus: '',
          note: ''
        },
        dataRule: {
          orderId: [
            { required: true, message: '¶©µ¥id不能为空', trigger: 'blur' }
          ],
          operateMan: [
            { required: true, message: '²Ù×÷ÈË[ÓÃ»§£»ÏµÍ³£»ºóÌ¨¹ÜÀíÔ±]不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '²Ù×÷Ê±¼ä不能为空', trigger: 'blur' }
          ],
          orderStatus: [
            { required: true, message: '¶©µ¥×´Ì¬¡¾0->´ý¸¶¿î£»1->´ý·¢»õ£»2->ÒÑ·¢»õ£»3->ÒÑÍê³É£»4->ÒÑ¹Ø±Õ£»5->ÎÞÐ§¶©µ¥¡¿不能为空', trigger: 'blur' }
          ],
          note: [
            { required: true, message: '±¸×¢不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/order/orderoperatehistory/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.orderId = data.orderOperateHistory.orderId
                this.dataForm.operateMan = data.orderOperateHistory.operateMan
                this.dataForm.createTime = data.orderOperateHistory.createTime
                this.dataForm.orderStatus = data.orderOperateHistory.orderStatus
                this.dataForm.note = data.orderOperateHistory.note
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
              url: this.$http.adornUrl(`/order/orderoperatehistory/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'orderId': this.dataForm.orderId,
                'operateMan': this.dataForm.operateMan,
                'createTime': this.dataForm.createTime,
                'orderStatus': this.dataForm.orderStatus,
                'note': this.dataForm.note
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

<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="»î¶¯±êÌâ" prop="title">
      <el-input v-model="dataForm.title" placeholder="»î¶¯±êÌâ"></el-input>
    </el-form-item>
    <el-form-item label="¿ªÊ¼ÈÕÆÚ" prop="startTime">
      <el-input v-model="dataForm.startTime" placeholder="¿ªÊ¼ÈÕÆÚ"></el-input>
    </el-form-item>
    <el-form-item label="½áÊøÈÕÆÚ" prop="endTime">
      <el-input v-model="dataForm.endTime" placeholder="½áÊøÈÕÆÚ"></el-input>
    </el-form-item>
    <el-form-item label="ÉÏÏÂÏß×´Ì¬" prop="status">
      <el-input v-model="dataForm.status" placeholder="ÉÏÏÂÏß×´Ì¬"></el-input>
    </el-form-item>
    <el-form-item label="´´½¨Ê±¼ä" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="´´½¨Ê±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="´´½¨ÈË" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="´´½¨ÈË"></el-input>
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
          title: '',
          startTime: '',
          endTime: '',
          status: '',
          createTime: '',
          userId: ''
        },
        dataRule: {
          title: [
            { required: true, message: '»î¶¯±êÌâ不能为空', trigger: 'blur' }
          ],
          startTime: [
            { required: true, message: '¿ªÊ¼ÈÕÆÚ不能为空', trigger: 'blur' }
          ],
          endTime: [
            { required: true, message: '½áÊøÈÕÆÚ不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: 'ÉÏÏÂÏß×´Ì¬不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '´´½¨Ê±¼ä不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '´´½¨ÈË不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/coupon/seckillpromotion/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.title = data.seckillPromotion.title
                this.dataForm.startTime = data.seckillPromotion.startTime
                this.dataForm.endTime = data.seckillPromotion.endTime
                this.dataForm.status = data.seckillPromotion.status
                this.dataForm.createTime = data.seckillPromotion.createTime
                this.dataForm.userId = data.seckillPromotion.userId
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
              url: this.$http.adornUrl(`/coupon/seckillpromotion/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'title': this.dataForm.title,
                'startTime': this.dataForm.startTime,
                'endTime': this.dataForm.endTime,
                'status': this.dataForm.status,
                'createTime': this.dataForm.createTime,
                'userId': this.dataForm.userId
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

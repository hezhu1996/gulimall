<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="³É³¤»ý·Ö" prop="growBounds">
      <el-input v-model="dataForm.growBounds" placeholder="³É³¤»ý·Ö"></el-input>
    </el-form-item>
    <el-form-item label="¹ºÎï»ý·Ö" prop="buyBounds">
      <el-input v-model="dataForm.buyBounds" placeholder="¹ºÎï»ý·Ö"></el-input>
    </el-form-item>
    <el-form-item label="ÓÅ»ÝÉúÐ§Çé¿ö[1111£¨ËÄ¸ö×´Ì¬Î»£¬´ÓÓÒµ½×ó£©;0 - ÎÞÓÅ»Ý£¬³É³¤»ý·ÖÊÇ·ñÔùËÍ;1 - ÎÞÓÅ»Ý£¬¹ºÎï»ý·ÖÊÇ·ñÔùËÍ;2 - ÓÐÓÅ»Ý£¬³É³¤»ý·ÖÊÇ·ñÔùËÍ;3 - ÓÐÓÅ»Ý£¬¹ºÎï»ý·ÖÊÇ·ñÔùËÍ¡¾×´Ì¬Î»0£º²»ÔùËÍ£¬1£ºÔùËÍ¡¿]" prop="work">
      <el-input v-model="dataForm.work" placeholder="ÓÅ»ÝÉúÐ§Çé¿ö[1111£¨ËÄ¸ö×´Ì¬Î»£¬´ÓÓÒµ½×ó£©;0 - ÎÞÓÅ»Ý£¬³É³¤»ý·ÖÊÇ·ñÔùËÍ;1 - ÎÞÓÅ»Ý£¬¹ºÎï»ý·ÖÊÇ·ñÔùËÍ;2 - ÓÐÓÅ»Ý£¬³É³¤»ý·ÖÊÇ·ñÔùËÍ;3 - ÓÐÓÅ»Ý£¬¹ºÎï»ý·ÖÊÇ·ñÔùËÍ¡¾×´Ì¬Î»0£º²»ÔùËÍ£¬1£ºÔùËÍ¡¿]"></el-input>
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
          spuId: '',
          growBounds: '',
          buyBounds: '',
          work: ''
        },
        dataRule: {
          spuId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          growBounds: [
            { required: true, message: '³É³¤»ý·Ö不能为空', trigger: 'blur' }
          ],
          buyBounds: [
            { required: true, message: '¹ºÎï»ý·Ö不能为空', trigger: 'blur' }
          ],
          work: [
            { required: true, message: 'ÓÅ»ÝÉúÐ§Çé¿ö[1111£¨ËÄ¸ö×´Ì¬Î»£¬´ÓÓÒµ½×ó£©;0 - ÎÞÓÅ»Ý£¬³É³¤»ý·ÖÊÇ·ñÔùËÍ;1 - ÎÞÓÅ»Ý£¬¹ºÎï»ý·ÖÊÇ·ñÔùËÍ;2 - ÓÐÓÅ»Ý£¬³É³¤»ý·ÖÊÇ·ñÔùËÍ;3 - ÓÐÓÅ»Ý£¬¹ºÎï»ý·ÖÊÇ·ñÔùËÍ¡¾×´Ì¬Î»0£º²»ÔùËÍ£¬1£ºÔùËÍ¡¿]不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/coupon/spubounds/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.spuId = data.spuBounds.spuId
                this.dataForm.growBounds = data.spuBounds.growBounds
                this.dataForm.buyBounds = data.spuBounds.buyBounds
                this.dataForm.work = data.spuBounds.work
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
              url: this.$http.adornUrl(`/coupon/spubounds/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'spuId': this.dataForm.spuId,
                'growBounds': this.dataForm.growBounds,
                'buyBounds': this.dataForm.buyBounds,
                'work': this.dataForm.work
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

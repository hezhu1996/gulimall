<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="ÉÌÆ·id" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder="ÉÌÆ·id"></el-input>
    </el-form-item>
    <el-form-item label="ÊôÐÔid" prop="attrId">
      <el-input v-model="dataForm.attrId" placeholder="ÊôÐÔid"></el-input>
    </el-form-item>
    <el-form-item label="ÊôÐÔÃû" prop="attrName">
      <el-input v-model="dataForm.attrName" placeholder="ÊôÐÔÃû"></el-input>
    </el-form-item>
    <el-form-item label="ÊôÐÔÖµ" prop="attrValue">
      <el-input v-model="dataForm.attrValue" placeholder="ÊôÐÔÖµ"></el-input>
    </el-form-item>
    <el-form-item label="Ë³Ðò" prop="attrSort">
      <el-input v-model="dataForm.attrSort" placeholder="Ë³Ðò"></el-input>
    </el-form-item>
    <el-form-item label="¿ìËÙÕ¹Ê¾¡¾ÊÇ·ñÕ¹Ê¾ÔÚ½éÉÜÉÏ£»0-·ñ 1-ÊÇ¡¿" prop="quickShow">
      <el-input v-model="dataForm.quickShow" placeholder="¿ìËÙÕ¹Ê¾¡¾ÊÇ·ñÕ¹Ê¾ÔÚ½éÉÜÉÏ£»0-·ñ 1-ÊÇ¡¿"></el-input>
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
          attrId: '',
          attrName: '',
          attrValue: '',
          attrSort: '',
          quickShow: ''
        },
        dataRule: {
          spuId: [
            { required: true, message: 'ÉÌÆ·id不能为空', trigger: 'blur' }
          ],
          attrId: [
            { required: true, message: 'ÊôÐÔid不能为空', trigger: 'blur' }
          ],
          attrName: [
            { required: true, message: 'ÊôÐÔÃû不能为空', trigger: 'blur' }
          ],
          attrValue: [
            { required: true, message: 'ÊôÐÔÖµ不能为空', trigger: 'blur' }
          ],
          attrSort: [
            { required: true, message: 'Ë³Ðò不能为空', trigger: 'blur' }
          ],
          quickShow: [
            { required: true, message: '¿ìËÙÕ¹Ê¾¡¾ÊÇ·ñÕ¹Ê¾ÔÚ½éÉÜÉÏ£»0-·ñ 1-ÊÇ¡¿不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/product/productattrvalue/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.spuId = data.productAttrValue.spuId
                this.dataForm.attrId = data.productAttrValue.attrId
                this.dataForm.attrName = data.productAttrValue.attrName
                this.dataForm.attrValue = data.productAttrValue.attrValue
                this.dataForm.attrSort = data.productAttrValue.attrSort
                this.dataForm.quickShow = data.productAttrValue.quickShow
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
              url: this.$http.adornUrl(`/product/productattrvalue/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'spuId': this.dataForm.spuId,
                'attrId': this.dataForm.attrId,
                'attrName': this.dataForm.attrName,
                'attrValue': this.dataForm.attrValue,
                'attrSort': this.dataForm.attrSort,
                'quickShow': this.dataForm.quickShow
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

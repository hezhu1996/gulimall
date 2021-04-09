<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="Æ·ÅÆÃû" prop="name">
      <el-input v-model="dataForm.name" placeholder="Æ·ÅÆÃû"></el-input>
    </el-form-item>
    <el-form-item label="Æ·ÅÆlogoµØÖ·" prop="logo">
      <el-input v-model="dataForm.logo" placeholder="Æ·ÅÆlogoµØÖ·"></el-input>
    </el-form-item>
    <el-form-item label="½éÉÜ" prop="descript">
      <el-input v-model="dataForm.descript" placeholder="½éÉÜ"></el-input>
    </el-form-item>
    <el-form-item label="ÏÔÊ¾×´Ì¬[0-²»ÏÔÊ¾£»1-ÏÔÊ¾]" prop="showStatus">
      <el-input v-model="dataForm.showStatus" placeholder="ÏÔÊ¾×´Ì¬[0-²»ÏÔÊ¾£»1-ÏÔÊ¾]"></el-input>
    </el-form-item>
    <el-form-item label="¼ìË÷Ê××ÖÄ¸" prop="firstLetter">
      <el-input v-model="dataForm.firstLetter" placeholder="¼ìË÷Ê××ÖÄ¸"></el-input>
    </el-form-item>
    <el-form-item label="ÅÅÐò" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="ÅÅÐò"></el-input>
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
          brandId: 0,
          name: '',
          logo: '',
          descript: '',
          showStatus: '',
          firstLetter: '',
          sort: ''
        },
        dataRule: {
          name: [
            { required: true, message: 'Æ·ÅÆÃû不能为空', trigger: 'blur' }
          ],
          logo: [
            { required: true, message: 'Æ·ÅÆlogoµØÖ·不能为空', trigger: 'blur' }
          ],
          descript: [
            { required: true, message: '½éÉÜ不能为空', trigger: 'blur' }
          ],
          showStatus: [
            { required: true, message: 'ÏÔÊ¾×´Ì¬[0-²»ÏÔÊ¾£»1-ÏÔÊ¾]不能为空', trigger: 'blur' }
          ],
          firstLetter: [
            { required: true, message: '¼ìË÷Ê××ÖÄ¸不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: 'ÅÅÐò不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.brandId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.brandId) {
            this.$http({
              url: this.$http.adornUrl(`/product/brand/info/${this.dataForm.brandId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.brand.name
                this.dataForm.logo = data.brand.logo
                this.dataForm.descript = data.brand.descript
                this.dataForm.showStatus = data.brand.showStatus
                this.dataForm.firstLetter = data.brand.firstLetter
                this.dataForm.sort = data.brand.sort
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
              url: this.$http.adornUrl(`/product/brand/${!this.dataForm.brandId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'brandId': this.dataForm.brandId || undefined,
                'name': this.dataForm.name,
                'logo': this.dataForm.logo,
                'descript': this.dataForm.descript,
                'showStatus': this.dataForm.showStatus,
                'firstLetter': this.dataForm.firstLetter,
                'sort': this.dataForm.sort
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

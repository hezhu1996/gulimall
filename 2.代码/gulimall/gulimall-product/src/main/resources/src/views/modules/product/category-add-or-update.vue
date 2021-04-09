<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="·ÖÀàÃû³Æ" prop="name">
      <el-input v-model="dataForm.name" placeholder="·ÖÀàÃû³Æ"></el-input>
    </el-form-item>
    <el-form-item label="¸¸·ÖÀàid" prop="parentCid">
      <el-input v-model="dataForm.parentCid" placeholder="¸¸·ÖÀàid"></el-input>
    </el-form-item>
    <el-form-item label="²ã¼¶" prop="catLevel">
      <el-input v-model="dataForm.catLevel" placeholder="²ã¼¶"></el-input>
    </el-form-item>
    <el-form-item label="ÊÇ·ñÏÔÊ¾[0-²»ÏÔÊ¾£¬1ÏÔÊ¾]" prop="showStatus">
      <el-input v-model="dataForm.showStatus" placeholder="ÊÇ·ñÏÔÊ¾[0-²»ÏÔÊ¾£¬1ÏÔÊ¾]"></el-input>
    </el-form-item>
    <el-form-item label="ÅÅÐò" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="ÅÅÐò"></el-input>
    </el-form-item>
    <el-form-item label="Í¼±êµØÖ·" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="Í¼±êµØÖ·"></el-input>
    </el-form-item>
    <el-form-item label="¼ÆÁ¿µ¥Î»" prop="productUnit">
      <el-input v-model="dataForm.productUnit" placeholder="¼ÆÁ¿µ¥Î»"></el-input>
    </el-form-item>
    <el-form-item label="ÉÌÆ·ÊýÁ¿" prop="productCount">
      <el-input v-model="dataForm.productCount" placeholder="ÉÌÆ·ÊýÁ¿"></el-input>
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
          catId: 0,
          name: '',
          parentCid: '',
          catLevel: '',
          showStatus: '',
          sort: '',
          icon: '',
          productUnit: '',
          productCount: ''
        },
        dataRule: {
          name: [
            { required: true, message: '·ÖÀàÃû³Æ不能为空', trigger: 'blur' }
          ],
          parentCid: [
            { required: true, message: '¸¸·ÖÀàid不能为空', trigger: 'blur' }
          ],
          catLevel: [
            { required: true, message: '²ã¼¶不能为空', trigger: 'blur' }
          ],
          showStatus: [
            { required: true, message: 'ÊÇ·ñÏÔÊ¾[0-²»ÏÔÊ¾£¬1ÏÔÊ¾]不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: 'ÅÅÐò不能为空', trigger: 'blur' }
          ],
          icon: [
            { required: true, message: 'Í¼±êµØÖ·不能为空', trigger: 'blur' }
          ],
          productUnit: [
            { required: true, message: '¼ÆÁ¿µ¥Î»不能为空', trigger: 'blur' }
          ],
          productCount: [
            { required: true, message: 'ÉÌÆ·ÊýÁ¿不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.catId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.catId) {
            this.$http({
              url: this.$http.adornUrl(`/product/category/info/${this.dataForm.catId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.category.name
                this.dataForm.parentCid = data.category.parentCid
                this.dataForm.catLevel = data.category.catLevel
                this.dataForm.showStatus = data.category.showStatus
                this.dataForm.sort = data.category.sort
                this.dataForm.icon = data.category.icon
                this.dataForm.productUnit = data.category.productUnit
                this.dataForm.productCount = data.category.productCount
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
              url: this.$http.adornUrl(`/product/category/${!this.dataForm.catId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'catId': this.dataForm.catId || undefined,
                'name': this.dataForm.name,
                'parentCid': this.dataForm.parentCid,
                'catLevel': this.dataForm.catLevel,
                'showStatus': this.dataForm.showStatus,
                'sort': this.dataForm.sort,
                'icon': this.dataForm.icon,
                'productUnit': this.dataForm.productUnit,
                'productCount': this.dataForm.productCount
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

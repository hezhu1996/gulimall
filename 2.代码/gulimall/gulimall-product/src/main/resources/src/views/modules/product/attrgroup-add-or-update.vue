<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="×éÃû" prop="attrGroupName">
      <el-input v-model="dataForm.attrGroupName" placeholder="×éÃû"></el-input>
    </el-form-item>
    <el-form-item label="ÅÅÐò" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="ÅÅÐò"></el-input>
    </el-form-item>
    <el-form-item label="ÃèÊö" prop="descript">
      <el-input v-model="dataForm.descript" placeholder="ÃèÊö"></el-input>
    </el-form-item>
    <el-form-item label="×éÍ¼±ê" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="×éÍ¼±ê"></el-input>
    </el-form-item>
    <el-form-item label="ËùÊô·ÖÀàid" prop="catelogId">
      <el-input v-model="dataForm.catelogId" placeholder="ËùÊô·ÖÀàid"></el-input>
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
          attrGroupId: 0,
          attrGroupName: '',
          sort: '',
          descript: '',
          icon: '',
          catelogId: ''
        },
        dataRule: {
          attrGroupName: [
            { required: true, message: '×éÃû不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: 'ÅÅÐò不能为空', trigger: 'blur' }
          ],
          descript: [
            { required: true, message: 'ÃèÊö不能为空', trigger: 'blur' }
          ],
          icon: [
            { required: true, message: '×éÍ¼±ê不能为空', trigger: 'blur' }
          ],
          catelogId: [
            { required: true, message: 'ËùÊô·ÖÀàid不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.attrGroupId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.attrGroupId) {
            this.$http({
              url: this.$http.adornUrl(`/product/attrgroup/info/${this.dataForm.attrGroupId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.attrGroupName = data.attrGroup.attrGroupName
                this.dataForm.sort = data.attrGroup.sort
                this.dataForm.descript = data.attrGroup.descript
                this.dataForm.icon = data.attrGroup.icon
                this.dataForm.catelogId = data.attrGroup.catelogId
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
              url: this.$http.adornUrl(`/product/attrgroup/${!this.dataForm.attrGroupId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'attrGroupId': this.dataForm.attrGroupId || undefined,
                'attrGroupName': this.dataForm.attrGroupName,
                'sort': this.dataForm.sort,
                'descript': this.dataForm.descript,
                'icon': this.dataForm.icon,
                'catelogId': this.dataForm.catelogId
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

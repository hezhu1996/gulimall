<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="spu_id" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder="spu_id"></el-input>
    </el-form-item>
    <el-form-item label="Í¼Æ¬Ãû" prop="imgName">
      <el-input v-model="dataForm.imgName" placeholder="Í¼Æ¬Ãû"></el-input>
    </el-form-item>
    <el-form-item label="Í¼Æ¬µØÖ·" prop="imgUrl">
      <el-input v-model="dataForm.imgUrl" placeholder="Í¼Æ¬µØÖ·"></el-input>
    </el-form-item>
    <el-form-item label="Ë³Ðò" prop="imgSort">
      <el-input v-model="dataForm.imgSort" placeholder="Ë³Ðò"></el-input>
    </el-form-item>
    <el-form-item label="ÊÇ·ñÄ¬ÈÏÍ¼" prop="defaultImg">
      <el-input v-model="dataForm.defaultImg" placeholder="ÊÇ·ñÄ¬ÈÏÍ¼"></el-input>
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
          imgName: '',
          imgUrl: '',
          imgSort: '',
          defaultImg: ''
        },
        dataRule: {
          spuId: [
            { required: true, message: 'spu_id不能为空', trigger: 'blur' }
          ],
          imgName: [
            { required: true, message: 'Í¼Æ¬Ãû不能为空', trigger: 'blur' }
          ],
          imgUrl: [
            { required: true, message: 'Í¼Æ¬µØÖ·不能为空', trigger: 'blur' }
          ],
          imgSort: [
            { required: true, message: 'Ë³Ðò不能为空', trigger: 'blur' }
          ],
          defaultImg: [
            { required: true, message: 'ÊÇ·ñÄ¬ÈÏÍ¼不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/product/spuimages/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.spuId = data.spuImages.spuId
                this.dataForm.imgName = data.spuImages.imgName
                this.dataForm.imgUrl = data.spuImages.imgUrl
                this.dataForm.imgSort = data.spuImages.imgSort
                this.dataForm.defaultImg = data.spuImages.defaultImg
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
              url: this.$http.adornUrl(`/product/spuimages/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'spuId': this.dataForm.spuId,
                'imgName': this.dataForm.imgName,
                'imgUrl': this.dataForm.imgUrl,
                'imgSort': this.dataForm.imgSort,
                'defaultImg': this.dataForm.defaultImg
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

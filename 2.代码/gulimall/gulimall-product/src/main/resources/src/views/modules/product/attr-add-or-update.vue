<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="ÊôÐÔÃû" prop="attrName">
      <el-input v-model="dataForm.attrName" placeholder="ÊôÐÔÃû"></el-input>
    </el-form-item>
    <el-form-item label="ÊÇ·ñÐèÒª¼ìË÷[0-²»ÐèÒª£¬1-ÐèÒª]" prop="searchType">
      <el-input v-model="dataForm.searchType" placeholder="ÊÇ·ñÐèÒª¼ìË÷[0-²»ÐèÒª£¬1-ÐèÒª]"></el-input>
    </el-form-item>
    <el-form-item label="ÊôÐÔÍ¼±ê" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="ÊôÐÔÍ¼±ê"></el-input>
    </el-form-item>
    <el-form-item label="¿ÉÑ¡ÖµÁÐ±í[ÓÃ¶ººÅ·Ö¸ô]" prop="valueSelect">
      <el-input v-model="dataForm.valueSelect" placeholder="¿ÉÑ¡ÖµÁÐ±í[ÓÃ¶ººÅ·Ö¸ô]"></el-input>
    </el-form-item>
    <el-form-item label="ÊôÐÔÀàÐÍ[0-ÏúÊÛÊôÐÔ£¬1-»ù±¾ÊôÐÔ£¬2-¼ÈÊÇÏúÊÛÊôÐÔÓÖÊÇ»ù±¾ÊôÐÔ]" prop="attrType">
      <el-input v-model="dataForm.attrType" placeholder="ÊôÐÔÀàÐÍ[0-ÏúÊÛÊôÐÔ£¬1-»ù±¾ÊôÐÔ£¬2-¼ÈÊÇÏúÊÛÊôÐÔÓÖÊÇ»ù±¾ÊôÐÔ]"></el-input>
    </el-form-item>
    <el-form-item label="ÆôÓÃ×´Ì¬[0 - ½ûÓÃ£¬1 - ÆôÓÃ]" prop="enable">
      <el-input v-model="dataForm.enable" placeholder="ÆôÓÃ×´Ì¬[0 - ½ûÓÃ£¬1 - ÆôÓÃ]"></el-input>
    </el-form-item>
    <el-form-item label="ËùÊô·ÖÀà" prop="catelogId">
      <el-input v-model="dataForm.catelogId" placeholder="ËùÊô·ÖÀà"></el-input>
    </el-form-item>
    <el-form-item label="¿ìËÙÕ¹Ê¾¡¾ÊÇ·ñÕ¹Ê¾ÔÚ½éÉÜÉÏ£»0-·ñ 1-ÊÇ¡¿£¬ÔÚskuÖÐÈÔÈ»¿ÉÒÔµ÷Õû" prop="showDesc">
      <el-input v-model="dataForm.showDesc" placeholder="¿ìËÙÕ¹Ê¾¡¾ÊÇ·ñÕ¹Ê¾ÔÚ½éÉÜÉÏ£»0-·ñ 1-ÊÇ¡¿£¬ÔÚskuÖÐÈÔÈ»¿ÉÒÔµ÷Õû"></el-input>
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
          attrId: 0,
          attrName: '',
          searchType: '',
          icon: '',
          valueSelect: '',
          attrType: '',
          enable: '',
          catelogId: '',
          showDesc: ''
        },
        dataRule: {
          attrName: [
            { required: true, message: 'ÊôÐÔÃû不能为空', trigger: 'blur' }
          ],
          searchType: [
            { required: true, message: 'ÊÇ·ñÐèÒª¼ìË÷[0-²»ÐèÒª£¬1-ÐèÒª]不能为空', trigger: 'blur' }
          ],
          icon: [
            { required: true, message: 'ÊôÐÔÍ¼±ê不能为空', trigger: 'blur' }
          ],
          valueSelect: [
            { required: true, message: '¿ÉÑ¡ÖµÁÐ±í[ÓÃ¶ººÅ·Ö¸ô]不能为空', trigger: 'blur' }
          ],
          attrType: [
            { required: true, message: 'ÊôÐÔÀàÐÍ[0-ÏúÊÛÊôÐÔ£¬1-»ù±¾ÊôÐÔ£¬2-¼ÈÊÇÏúÊÛÊôÐÔÓÖÊÇ»ù±¾ÊôÐÔ]不能为空', trigger: 'blur' }
          ],
          enable: [
            { required: true, message: 'ÆôÓÃ×´Ì¬[0 - ½ûÓÃ£¬1 - ÆôÓÃ]不能为空', trigger: 'blur' }
          ],
          catelogId: [
            { required: true, message: 'ËùÊô·ÖÀà不能为空', trigger: 'blur' }
          ],
          showDesc: [
            { required: true, message: '¿ìËÙÕ¹Ê¾¡¾ÊÇ·ñÕ¹Ê¾ÔÚ½éÉÜÉÏ£»0-·ñ 1-ÊÇ¡¿£¬ÔÚskuÖÐÈÔÈ»¿ÉÒÔµ÷Õû不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.attrId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.attrId) {
            this.$http({
              url: this.$http.adornUrl(`/product/attr/info/${this.dataForm.attrId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.attrName = data.attr.attrName
                this.dataForm.searchType = data.attr.searchType
                this.dataForm.icon = data.attr.icon
                this.dataForm.valueSelect = data.attr.valueSelect
                this.dataForm.attrType = data.attr.attrType
                this.dataForm.enable = data.attr.enable
                this.dataForm.catelogId = data.attr.catelogId
                this.dataForm.showDesc = data.attr.showDesc
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
              url: this.$http.adornUrl(`/product/attr/${!this.dataForm.attrId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'attrId': this.dataForm.attrId || undefined,
                'attrName': this.dataForm.attrName,
                'searchType': this.dataForm.searchType,
                'icon': this.dataForm.icon,
                'valueSelect': this.dataForm.valueSelect,
                'attrType': this.dataForm.attrType,
                'enable': this.dataForm.enable,
                'catelogId': this.dataForm.catelogId,
                'showDesc': this.dataForm.showDesc
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

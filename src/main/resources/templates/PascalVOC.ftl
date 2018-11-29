<annotation>
    <folder>person</folder>
    <filename>${imagename}</filename>
    <path>unknown</path>
    <source>
        <database>Unknown</database>
    </source>
    <size>
        <width>${size.width}</width>
        <height>${size.height}</height>
        <depth>3</depth>
    </size>
    <segmented>0</segmented>
    <#list shapes as item>
    <object>
        <name>${item.category!}</name>
        <pose>${item.label!}</pose>
        <truncated>0</truncated>
        <difficult>0</difficult>
        <bndbox>
            <xmin>${item.bbox.x?round}</xmin>
            <ymin>${item.bbox.y?round}</ymin>
            <xmax>${(item.bbox.x + item.bbox.w)?round}</xmax>
            <ymax>${(item.bbox.y + item.bbox.h)?round}</ymax>
        </bndbox>
    </object>
    </#list>
</annotation>

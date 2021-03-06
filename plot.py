import matplotlib.pyplot as plt
import json
import matplotlib as mpl


def readFile(filename):
    text = ""
    with open(filename, "r") as f:
        for line in f:
            text += line
    sourcefileDict = json.loads(text)
    return sourcefileDict

file = "./src/test/resources/mesh_x_sin_cos_20000.json"
jsonDict = readFile(file)
elementList = jsonDict["elements"]
valueList = jsonDict["values"]
nodeList = jsonDict["nodes"]
vertices = {}

valueDict = {}
for item in valueList:
    itemId = item["element_id"]
    value = item["value"]
    valueDict[itemId] = value

# convert nodes
for nodeItem in nodeList:
    nodeId = nodeItem["id"]
    x = nodeItem["x"]
    y = nodeItem["y"]
    vertices[nodeId] = {"x" : x, "y" : y}

valuesWoElementList = [i["value"] for i in valueList]
minValue = min(valuesWoElementList)
maxValue = max(valuesWoElementList)
norm = mpl.colors.Normalize(vmin = minValue, vmax = maxValue)

hills = readFile("./hills.json")
hillDict = {}
hillIds = set()
peaks = []
for item in hills:
    hillId = item["hillId"]
    hillIds.add(hillId)
    peaks.append(item["peak"])
    elements = item["elementIds"]
    for element in elements:
        hillDict[element] = hillId
print(peaks)


cmap = mpl.cm.get_cmap('Greys')
cmap2 = mpl.cm.get_cmap('rainbow')
fig = plt.figure(figsize = (16, 9), dpi = 120)
ax = fig.add_subplot(121)
for polygon in elementList:
    elementId = polygon["id"]
    vertexIds = polygon["nodes"]
    nodeVertices = []
    for vertexId in vertexIds:
        nodeVertices.append([vertices[vertexId]["x"], vertices[vertexId]["y"]])
    nodeVertices.append(nodeVertices[0])
    x = [i[0] for i in nodeVertices]
    y = [i[1] for i in nodeVertices]
    plt.plot(x, y, 'k')

    value = norm(valueDict[elementId])
    rgba = cmap(value)
    pl = plt.Polygon(nodeVertices[:-1], color = rgba if not elementId in peaks else 'm')
    ax.add_patch(pl)
    plt.xlabel("elevation (gray), peaks (magenta)")
   
norm2 = mpl.colors.Normalize(vmin = min(hillIds), vmax = max(hillIds))
ax = fig.add_subplot(122)
for polygon in elementList:
    elementId = polygon["id"]
    vertexIds = polygon["nodes"]
    nodeVertices = []
    for vertexId in vertexIds:
        nodeVertices.append([vertices[vertexId]["x"], vertices[vertexId]["y"]])
    nodeVertices.append(nodeVertices[0])
    x = [i[0] for i in nodeVertices]
    y = [i[1] for i in nodeVertices]
    plt.plot(x, y, 'k')

    value = norm2(hillDict[elementId])
    rgba = cmap2(value)
    pl = plt.Polygon(nodeVertices[:-1], color = rgba if not elementId in peaks else 'k')
    ax.add_patch(pl)
    plt.xlabel("assignment to region (color), peaks (black)")
plt.tight_layout()
plt.savefig("assignment_sin_cos_20000.pdf")
plt.show()
